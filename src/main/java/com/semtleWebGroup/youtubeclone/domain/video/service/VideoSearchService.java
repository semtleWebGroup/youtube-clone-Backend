package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoPageResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoSearchService {
    private final VideoRepository videoRepository;

    public VideoPageResponse searchByTitleContaining(String keyword, Pageable pageable) {
        Page<Video> videoPage = videoRepository.searchVideosByTitleContainingOrderByCreatedTimeDesc(keyword, pageable);
        VideoPageResponse result;

        if (videoPage.isEmpty()) {
            // 검색 결과가 없는 경우 유사한 비디오를 추천
            Page<Video> recommendedVideos = searchVideosBySimilarity(keyword, pageable);
            result = new VideoPageResponse(recommendedVideos);
        } else {
            List<Video> combinedVideos = new ArrayList<>(videoPage.getContent());

            // 검색 결과 이후에 유사한 비디오를 추가로 가져옴
            List<Video> additionalVideos = searchVideosBySimilarity(keyword, pageable)
                    .stream()
                    .filter(video -> !videoPage.getContent().contains(video))
                    .collect(Collectors.toList());

            combinedVideos.addAll(additionalVideos);
            result = new VideoPageResponse(new PageImpl<>(combinedVideos, pageable, combinedVideos.size()));
        }

        return result;
    }

    public Page<Video> searchVideosBySimilarity(String queryText, Pageable pageable) {
        List<Video> videos = videoRepository.findAll();

        // 벡터화 함수를 통해 검색어를 벡터로 변환
        double[] queryVector = vectorizeText(queryText);

        // 코사인 유사도 계산하여 비디오 리스트를 정렬
        videos.sort(Comparator.comparingDouble(video -> calculateCosineSimilarity(queryVector, vectorizeText(video.getTitle()))));
        Collections.reverse(videos);

        // 페이징 처리하여 결과 반환
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), videos.size());
        List<Video> paginatedVideos = videos.subList(start, end);

        return new PageImpl<>(paginatedVideos, pageable, videos.size());
    }

    private double[] vectorizeText(String text) {
        // 텍스트를 벡터로 변환하는 로직 구현
        // 의미를 가진 벡터로 변환한다면 머신러닝 학습 필요

        //  단어 빈도수로 벡터화
        String[] words = text.toLowerCase().split(" ");
        double[] vector = new double[words.length];

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // 단어 빈도수 계산하여 벡터에 할당
            vector[i] = calculateWordFrequency(word, words);
        }

        return vector;

    }

    private double calculateCosineSimilarity(double[] vector1, double[] vector2) {
        // 코사인 유사도 계산 로직 구현
        // 두 벡터의 내적을 계산한 후, 각 벡터의 크기의 곱으로 나누어줌

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));

        return similarity;
    }

    private double calculateWordFrequency(String word, String[] words) {
        // 단어 빈도수 계산 로직 구현
        int frequency = 0;
        for (String w : words) {
            if (w.equals(word)) {
                frequency++;
            }
        }
        return frequency;
    }

    public VideoPageResponse getAllVideosRandomOrder(Pageable pageable) {
        List<Video> allVideos = videoRepository.findAll();
        int totalVideos = allVideos.size();

        List<Video> randomizedVideos = shuffleList(allVideos);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), randomizedVideos.size());
        List<Video> paginatedVideos = randomizedVideos.subList(start, end);

        Page<Video> result = new PageImpl<>(paginatedVideos, pageable, totalVideos);

        return new VideoPageResponse(result);
    }

    /**
     * 비디오 리스트를 랜덤으로 섞는 메소드
     * @param videos
     * @return
     */
    private List<Video> shuffleList(List<Video> videos) {
        List<Video> shuffledList = new ArrayList<>(videos);
        Random random = new Random();

        for (int i = shuffledList.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Video temp = shuffledList.get(index);
            shuffledList.set(index, shuffledList.get(i));
            shuffledList.set(i, temp);
        }

        return shuffledList;
    }
}
