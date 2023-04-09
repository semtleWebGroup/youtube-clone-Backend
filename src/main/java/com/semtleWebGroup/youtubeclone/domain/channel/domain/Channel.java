package com.semtleWebGroup.youtubeclone.domain.channel.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
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
    @JoinTable(name = "subscription",
    joinColumns = @JoinColumn(name = "channel_id"),
    inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
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
