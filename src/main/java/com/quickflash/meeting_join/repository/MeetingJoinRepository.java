package com.quickflash.meeting_join.repository;

import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingJoinRepository extends JpaRepository<MeetingJoinEntity,Integer> {
    List<MeetingJoinEntity> findByPostId(int postId);
    Optional<MeetingJoinEntity> findByPostIdAndUserId(int postId, int userId);
    int countByPostId(int postId);
}

