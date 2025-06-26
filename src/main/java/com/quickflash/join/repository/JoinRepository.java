package com.quickflash.join.repository;

import com.quickflash.join.entity.JoinEntity;
import com.quickflash.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRepository extends JpaRepository<JoinEntity,Integer> {
    List<JoinEntity> findByPostId(int postId);
    Optional<JoinEntity> findByPostIdAndUserId(int postId, int userId);
}

