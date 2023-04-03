package com.semtleWebGroup.youtubeclone.domain.channel.repository;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    boolean existsByTitle(String title);
}
