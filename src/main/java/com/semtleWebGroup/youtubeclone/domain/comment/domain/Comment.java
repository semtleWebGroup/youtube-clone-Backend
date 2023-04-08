package com.semtleWebGroup.youtubeclone.domain.comment.domain;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name="comment")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "contents", nullable = false, length = 45)
    private String contents;

    @NotNull
    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    @Builder
    public Comment(String contents) {
        this.contents = contents;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
//
//    @OneToMany(mappedBy = "commentCommentid")
//    private Set<CommentLike> commentLikes = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "commentCommentid")
//    private Set<Comment> comments = new LinkedHashSet<>()

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "videoPageId", nullable = false)
//    private Video videoPageId;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "channelId", nullable = false)
//    private Channel channelId;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "commentCommentId", nullable = false)
//    private Comment commentCommentId;


}




