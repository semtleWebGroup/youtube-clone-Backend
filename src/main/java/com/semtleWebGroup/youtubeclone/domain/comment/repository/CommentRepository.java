package com.semtleWebGroup.youtubeclone.domain.comment.repository;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //List<Comment> findByVideo_VideoId(UUID idx);
}
