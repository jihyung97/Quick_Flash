package com.quickflash.api.strava.ability.service;

import com.quickflash.api.strava.OAuthClient.StravaTokenEntity;
import com.quickflash.api.strava.OAuthClient.StravaTokenResponse;
import com.quickflash.api.strava.ability.entity.AbilityEntity;
import com.quickflash.api.strava.ability.repository.AbilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Service
@Slf4j

public class AbilityBO {
   private final AbilityRepository abilityRepository;
    @Transactional
    public boolean addAbility(Double maxPowerOfCycle, Double maxSpeedOfRun, int userId) {
        AbilityEntity abilityEntity = abilityRepository.findById(userId)
                .orElseGet(() -> AbilityEntity.builder().userId(userId).build());

        if (maxPowerOfCycle != null && maxPowerOfCycle != 0.0) {
            abilityEntity.setMaxCyclingAvgPower(maxPowerOfCycle);
        }

        if (maxSpeedOfRun != null && maxSpeedOfRun != 0.0) {
            abilityEntity.setMaxRunningSpeed(maxSpeedOfRun);
        }

        abilityRepository.save(abilityEntity);
        return true;
    }


}
