package com.semtleWebGroup.youtubeclone.domain.member.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "commentid", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "contents", nullable = false, length = 45)
    private String contents;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private Instant createdTime;

    @NotNull
    @Column(name = "updated_time", nullable = false)
    private Instant updatedTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_videoid", nullable = false)
    private Video videoVideoid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_channelid", nullable = false)
    private Channel channelChannelid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_commentid", nullable = false)
    private Comment commentCommentid;

    @OneToMany(mappedBy = "commentCommentid")
    private Set<CommentLike> commentLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "commentCommentid")
    private Set<Comment> comments = new LinkedHashSet<>();

}