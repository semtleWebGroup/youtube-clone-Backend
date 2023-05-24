package com.semtleWebGroup.youtubeclone.domain.comment.repository;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByVideo_IdAndRootComment_Id(UUID videoId,Long rootCommentId, Pageable pageable);  //외래키인 video 검색해야함
    Page<Comment> findByRootComment_Id(Long idx, Pageable pageable);  //외래키인 comment 검색해야함
}
