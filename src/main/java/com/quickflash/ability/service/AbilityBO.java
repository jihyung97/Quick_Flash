package com.quickflash.ability.service;

import com.quickflash.ability.entity.AbilityEntity;
import com.quickflash.ability.repository.AbilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            abilityEntity.setMaxRunningAvgSpeed(maxSpeedOfRun);
        }

        abilityRepository.save(abilityEntity);
        return true;
    }
    public AbilityEntity getAbilityByUserId(int userId){
        return abilityRepository.findById(userId).orElse(null);
    }


}
