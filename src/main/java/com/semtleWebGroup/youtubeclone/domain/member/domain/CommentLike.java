package com.semtleWebGroup.youtubeclone.domain.member.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "comment_like")
public class CommentLike {
    @Id
    @Column(name = "comment_likeid", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_channelid", nullable = false)
    private Channel channelChannelid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_commentid", nullable = false)
    private Comment commentCommentid;

}