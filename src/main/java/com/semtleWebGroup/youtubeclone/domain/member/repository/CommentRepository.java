package com.semtleWebGroup.youtubeclone.domain.member.repository;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}