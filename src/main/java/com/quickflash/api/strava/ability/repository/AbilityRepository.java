package com.quickflash.api.strava.ability.repository;

import com.quickflash.api.strava.ability.entity.AbilityEntity;
import com.quickflash.meetingPost.entity.MeetingPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<AbilityEntity,Integer> {



}


