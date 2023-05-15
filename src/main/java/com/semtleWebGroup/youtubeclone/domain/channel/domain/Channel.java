package com.semtleWebGroup.youtubeclone.domain.channel.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    @Builder
    public Channel(String title, String description){
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

    // Helper method to add a video to the channel
    public void addVideo(Video video) {
        videos.add(video);
//        video.setChannel(this); TODO video에서 channel을 추가하는 메소드
    }

    // Helper method to remove a video from the channel
    public void removeVideo(Video video) {
        videos.remove(video);
//        video.setChannel(null); TODO video에서 channel을 삭제하는 메소드
    }
}
