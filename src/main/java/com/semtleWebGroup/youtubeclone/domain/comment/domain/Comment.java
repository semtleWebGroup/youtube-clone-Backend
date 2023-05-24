package com.semtleWebGroup.youtubeclone.domain.comment.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
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
    @OneToMany(orphanRemoval = true, mappedBy = "rootComment")
    private Set<Comment> replyComments = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_comment_id")
    private Comment rootComment;

    public void addReplyComment(Comment replyComment) {
        this.replyComments.add(replyComment);
        replyComment.setRootComment(this);
    }

    public void deleteReplyComment(Comment replyComment) {
        this.replyComments.remove(replyComment);
    }

    public int getReplyCount() { return this.replyComments.size();}  //리플 개수가 필요한 사람이 있을까 혹시나..

    @Builder
    public Comment(String contents) {
        this.contents = contents;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setRootComment(Comment rootComment) {
        this.rootComment = rootComment;
    }

    public void likeComment(CommentLike like) {
        this.likes.add(like);
        like.setComment(this);
    }

    public void unLikeComment(CommentLike like) {
        this.likes.remove(like);
    }

}




