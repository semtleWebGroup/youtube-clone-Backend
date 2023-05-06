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
import java.util.HashSet;
import java.util.Set;


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

    //@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    //@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    @Column(name = "likeCount")
    private Long likeCount;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<CommentLike> commentLikeList = new HashSet<>();
    public void mappingPostLike(CommentLike commentLike) {
        this.commentLikeList.add(commentLike);
    }

    public void updateLikeCount() {
        this.likeCount = (long) this.commentLikeList.size();
    }

    public void discountLike(CommentLike commentLike) {
        this.commentLikeList.remove(commentLike);
    }

}




