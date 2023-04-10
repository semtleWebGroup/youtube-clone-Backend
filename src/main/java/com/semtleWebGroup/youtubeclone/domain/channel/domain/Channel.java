package com.semtleWebGroup.youtubeclone.domain.channel.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
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
    @Column(name = "channelid", updatable = false)
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

    private String imageName;
    private String imagePath;


    @Builder
    public Channel(String title, String description){
        this.title = title;
        this.description = description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void update(String title, String description){
        if (title != null) this.title=title;
        if (description != null) this.description=description;
    }
}
