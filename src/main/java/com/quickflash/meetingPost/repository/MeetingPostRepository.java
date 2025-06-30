package com.quickflash.meetingPost.repository;

import com.quickflash.meetingPost.entity.MeetingPostEntity;
import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingPostRepository extends JpaRepository<MeetingPostEntity,Integer> {
    Optional<MeetingPostEntity > findById(int id);


}

