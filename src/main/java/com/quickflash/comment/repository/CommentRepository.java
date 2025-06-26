package com.quickflash.comment.repository;

import com.quickflash.comment.entity.CommentEntity;
import com.quickflash.join.entity.JoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {
    List<CommentEntity> findByPostId(int postId);

    Optional<JoinEntity> findByPostIdAndUserId(int postId, int userId);
}

