package com.semtleWebGroup.youtubeclone.domain.comment.repository;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRespository extends JpaRepository<CommentLike, Integer> {
}
