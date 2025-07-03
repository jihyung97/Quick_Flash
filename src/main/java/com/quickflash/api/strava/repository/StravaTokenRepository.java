package com.quickflash.api.strava.repository;

import com.quickflash.api.strava.OAuthClient.StravaTokenEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StravaTokenRepository extends JpaRepository<StravaTokenEntity,Integer> {


}

