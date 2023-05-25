package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="video")
@Getter
@NoArgsConstructor
public class Video {
    public enum VideoStatus{PUBLIC, PRIVATE, DRAFT};
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "video_id",columnDefinition = "BINARY(16)", nullable = false)
    private UUID id;

    @Column(length=45)
    private String title = "";

    @Column(length=45)
    private String description = "";

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private int viewCount = 0;

    private Long videoSec = 0L;

    @Enumerated(EnumType.STRING)
    private VideoStatus status = VideoStatus.PUBLIC; // 추후 디폴트 값은 DRAFT로 변경하고, PUBLIC/PRIVATE로 전환하는 기능 추가 필요.

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "video")
    private Set<VideoLike> likes = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "video")
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Builder
    public Video(Channel channel) {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.channel = channel;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        setUpdatedTime(LocalDateTime.now());
    }

    public void incrementViewCount() {
        this.viewCount += 1;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getLikeCount() { return this.likes.size(); }

    public Boolean isLike(Channel channel) {
        if (channel == null) return false;
        for (VideoLike vl : this.likes)
            if (vl.getChannel().equals(channel)) return true;
        return false;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void likeVideo(VideoLike like) {
        this.likes.add(like);
        like.setVideo(this);
    }

    public void unLikeVideo(VideoLike like) {
        this.likes.remove(like);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setVideo(this);
    }

    public void deleteComment(Comment comment) {
        for (Comment replyComment: comment.getReplyComments()) {   //부모는 아니지만 명시적인 처리를 위해서
            this.comments.remove(replyComment);
        }
        this.comments.remove(comment);
    }
}