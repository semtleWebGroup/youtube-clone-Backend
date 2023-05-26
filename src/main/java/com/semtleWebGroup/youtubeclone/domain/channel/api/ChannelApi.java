package com.semtleWebGroup.youtubeclone.domain.channel.api;

import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelService;
import com.semtleWebGroup.youtubeclone.domain.channel.application.SubscribeService;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelProfile;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelApi {
    private final ChannelService channelService;
    private final SubscribeService subscribeService;
    private final AccessToken.TokenBuilder tokenBuilder;

    /**
     * @param ChannelRequest form-data 형식으로 channelProfile.title, channelProfile.description, profile_img
     * @return 현재는 entity 자체를 반환. 프론트 사용 데이터 보고 overfetching 줄일 예정
     */
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity create(@Valid @RequestPart ChannelProfile dto,
                                 @RequestPart(required = false) MultipartFile profileImg,
                                 @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long memberId = Long.valueOf(fieldStringMap.get(AccessToken.Field.MEMBER_ID));

        ChannelDto channel = channelService.addChannel(dto,profileImg, memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    /**
     * @param channelId 구독할 채널, myid 로그인한 채널
     * @return 성공할 경우 OK, 실패할 경우 다른 ErrorResponse를 반환할 예정
     */
    @PostMapping("/{channelId}/subscribtion")
    public ResponseEntity subscribeChannel(@PathVariable("channelId")Long channelId,
                                           @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long myId = Long.valueOf(fieldStringMap.get(AccessToken.Field.CHANNEL_ID));

        subscribeService.subscribe(myId, channelId);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /**
     * 채널 정보 조회
     * @param channelId
     * @return 현재는 entity 자체를 반환. 프론트 사용 데이터 보고 overfetching 줄일 예정
     */
    @GetMapping("/{channelId}")
    public ResponseEntity getChannelInfo(@PathVariable("channelId")Long channelId){
        ChannelDto channel = channelService.getChannel(channelId);

        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }

    /**
     * @param channel 이름의 중복 제크
     * @return 중복이 아니면 OK, 중복이면 에러 코드 C001
     */
    @GetMapping("/validation")
    public ResponseEntity checkValidation(@RequestParam("name")String name){
        channelService.checkTitleValid(name);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /**
     * @param channelId 구독 리스트를 조회할 채널
     * @return channelId가 구독한 채널리스트 가져오기
     */
    @GetMapping("/{channelId}/subscribiton")
    public ResponseEntity getSubscribtionList(@PathVariable("channelId")Long channelId) {

        final Set<Channel> channelList = subscribeService.getSubscribedChannels(channelId);
        List<ChannelDto> response = channelList.stream().map(ChannelDto::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{channelId}/subscribers")
    public ResponseEntity displayCountOfSubscribers(@PathVariable Long channelId){

        Map<String,String> response = new HashMap<>();
        Long count = subscribeService.getCountOfSubscribers(channelId);
        response.put("count",count.toString());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * @param channelId 대상 채널
     * @param subscriberId 로그인 한 사용자 채널
     * @return
     */
    @GetMapping("/{channelId}/subscribed")
    public ResponseEntity checkSubscribe(@PathVariable Long channelId,
                                         @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long subscriberId = Long.valueOf(fieldStringMap.get(AccessToken.Field.CHANNEL_ID));

        Map<String,String> response = new HashMap<>();
        if (subscribeService.isSubscribed(channelId, subscriberId)){
            response.put("status","true");
        } else {
            response.put("status","false");
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 구독 취소
     * @param channelId - 구독을 취소할 채널 id, 채널 정보 수정 권한 확인할 토큰 필요.
     * @return 성공시 OK 실패시 다른 ErrorCode 예정
     */
    @DeleteMapping("/{channelId}/subscribtion")
    public ResponseEntity cancleSubscribtion(@PathVariable("channelId")Long channelId,
                                             @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long myId = Long.valueOf(fieldStringMap.get(AccessToken.Field.CHANNEL_ID));

        subscribeService.unsubscribe(myId, channelId);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    /**
     * 채널 삭제
     * @param channelId - 삭제할 채널id, 채널 정보 수정 권한 확인할 토큰 필요.
     * @return 성공시 OK 실패시 다른 ErrorCode 예정
     */
    @DeleteMapping("/{channelId}")
    public ResponseEntity deleteChannel(@PathVariable("channelId")Long channelId,
                                        @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long memberId = Long.valueOf(fieldStringMap.get(AccessToken.Field.MEMBER_ID));

        channelService.deleteChannel(channelId, memberId);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    /**
     * @param channelId 수정할 channelId
     * @param ChannelRequest form-data 형식으로 channelProfile.title, channelProfile.description, profile_img
     * @return 현재는 entity 자체를 반환. 프론트 사용 데이터 보고 overfetching 줄일 예정
     * @throws Exception 이미지 첨부 관련 Exception
     */
    @PatchMapping(value = "/{channelId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity editChannel(@PathVariable("channelId")Long channelId,
                                      @Valid @RequestPart ChannelProfile dto,
                                      @RequestPart(required = false) MultipartFile profileImg,
                                      @RequestHeader("Authorization") String authorization){
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        if (!isHeaderValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long memberId = Long.valueOf(fieldStringMap.get(AccessToken.Field.MEMBER_ID));

        final ChannelDto channel = channelService.updateChannel(channelId, dto, profileImg,memberId);


        return ResponseEntity.status(HttpStatus.OK).body(channel);
    }
}
