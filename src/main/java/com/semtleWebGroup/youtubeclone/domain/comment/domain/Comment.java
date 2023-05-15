package com.semtleWebGroup.youtubeclone.domain.comment.domain;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id" , nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "contents", length = 45)
    private String contents;

    @NotNull
    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public void update(String contents){
        if (contents != null) this.contents=contents;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "comment")
    private Set<CommentLike> likes = new HashSet<>();

    public int getLikeCount() { return this.likes.size();}

    public Boolean isLike(Channel channel) {
        if (channel == null) return false;
        for (CommentLike commentLike : this.likes)
            if (commentLike.getChannel().equals(channel)) return true;
        return false;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "rootComment_id")
    private Comment rootComment;

    @Builder
    public Comment(String contents, Video video, Channel channel, Comment rootComment) {
        this.contents = contents;
        this.video = video;
        this.channel = channel;
        this.rootComment = rootComment;
    }
}




