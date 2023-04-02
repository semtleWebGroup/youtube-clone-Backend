package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class VideoInfo {
    @NotNull(message="'title' cannot be null.")
    @Size(max=45, message="'title' length should be <= 45.")
    private String title;

    @Size(max=45, message="'description' length should be <= 45.")
    private String description;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

}
