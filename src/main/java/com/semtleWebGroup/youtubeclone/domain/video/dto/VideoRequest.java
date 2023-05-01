package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoRequest {
    @NotEmpty(message="'title' cannot be empty.")
    @Size(max=45, message="'title' length should be <= 45.")
    private String title;

    @NotEmpty(message="'description' cannot be empty.")
    @Size(max=45, message="'description' length should be <= 45.")
    private String description;
}
