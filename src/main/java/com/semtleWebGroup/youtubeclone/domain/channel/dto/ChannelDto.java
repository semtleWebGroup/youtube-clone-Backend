package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
import java.sql.SQLException;

@Getter
@Setter
public class ChannelDto {

    private Long id;
    private String title;
    private String description;
    private byte[] channelImage;

    public ChannelDto(Channel channel)  {
        this.id = channel.getId();
        this.title = channel.getTitle();
        this.description = channel.getDescription();
        declareChannelImage(channel.getProfileImg());
    }

    public void declareChannelImage(Blob channelImage) {
        if (channelImage != null) {
            try {
                this.channelImage = channelImage.getBytes(1, (int) channelImage.length());
            } catch (SQLException e) {
                // 예외 처리
            }
        }
    }
}

