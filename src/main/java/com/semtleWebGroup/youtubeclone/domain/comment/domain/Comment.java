package com.semtleWebGroup.youtubeclone.domain.comment.domain;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
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
    @Column(name = "comment_id", nullable = false)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public void update(String contents){
        if (contents != null) this.contents=contents;
        this.updatedTime = LocalDateTime.now();;
    }

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    private Integer root_comment_id;
}




