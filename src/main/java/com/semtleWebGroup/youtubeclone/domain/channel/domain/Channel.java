package com.semtleWebGroup.youtubeclone.domain.channel.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", updatable = false)
    private Long id;

    @Column(nullable = false, length = 15, unique = true)
    private String title;
    @Column(length = 70)
    private String description;

    @ManyToMany
    // 자기 참조로 M:N관계
    @JoinTable(name = "subscription",
            joinColumns = @JoinColumn(name = "channel_id"), // 엔티티와 매핑될 외래키 지정
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")    // 매핑될 다른 엔티티의 외래키 지정
    )
    // 구독 채널은 중복이 될 수 없으므로 set 사용
    private Set<Channel> subscribedChannels = new HashSet<>();

    // 구독자 수를 찾기 위해
    @ManyToMany(mappedBy = "subscribedChannels")
    private Set<Channel> subscribers = new HashSet<>();

    @Lob
    private Blob channelImage;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Video> videos = new HashSet<>();
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Video> videoLikeLists = new HashSet<>();
    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)    //cascade = CascadeType.ALL 사용시 오류 발생...
    private Set<Comment> comments = new HashSet<>();
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> commentsLikeLists = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Channel(String title, String description, Member member){
        this.member = member;
        this.title = title;
        this.description = description;
    }

    public void setChannelImage(Blob imageFile) {
        this.channelImage = imageFile;
    }



    public void update(String title, String description){
        if (title != null) this.title=title;
        if (description != null) this.description=description;
    }

    public void setSubscribedChannels(Set<Channel> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }

    public void setSubscribers(Set<Channel> subscribers) {
        this.subscribers = subscribers;
    }

    // channel에서 video를 추가하는 메소드
    public void addVideo(Video video) {
        videos.add(video);
        video.setChannel(this);
    }

    // channel에서 video를 삭제하는 메소드
    public void removeVideo(Video video) {
        videos.remove(video);

        // video 엔티티를 지울 때만 호출되기 때문에 이 줄은 없애도 될 것 같습니다.. ?!
//        video.setChannel(null);
    }

    public void likeVideo(Video video){
        videoLikeLists.add(video);
        // videoLike가 아니라 videoLikeList에서 video를 저장하기 때문에 video에서 channel을 지정할 필요는 없을 것 같습니다.
        // 추후 videoLike를 저장하게 된다면 ..? videoLike.setChannel()이 필요할 것 같습니다.
//        video.setChannel(this); TODO video에서 channel을 추가하는 메소드
    }

    public void unLikeVideo(Video video){
        videoLikeLists.remove(video);

        // 여기도 없애도 될 것 같습니다.
//        video.setChannel(null); TODO video에서 channel을 추가하는 메소드
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setChannel(this);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public void likeComment(Comment comment) {
        commentsLikeLists.add(comment);
    }

    public void unLikeComment(Comment comment) {
        commentsLikeLists.remove(comment);
    }
}
