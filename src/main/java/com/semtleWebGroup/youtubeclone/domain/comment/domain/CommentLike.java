package com.semtleWebGroup.youtubeclone.domain.comment.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="comment_like")
@Getter
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id", columnDefinition = "BIGINT", nullable = false)
    private Long commentLikeId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Builder
    public CommentLike(Channel channel, Comment comment) {
        this.channel = channel;
        this.comment = comment;
    }

}
