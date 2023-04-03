package com.semtleWebGroup.youtubeclone.domain.channel.repository;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
}