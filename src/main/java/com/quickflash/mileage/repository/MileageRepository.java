package com.quickflash.mileage.repository;

import com.quickflash.meetingPost.entity.MeetingPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MileageRepository extends JpaRepository<MeetingPostEntity,Integer> {
    Optional<MeetingPostEntity > findById(int id);


}

