package com.quickflash.api.strava.StravaApi;

import com.quickflash.api.strava.StravaApi.entity.DataForAbilityOfCycle;
import com.quickflash.api.strava.ability.service.AbilityBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class StravaApiService {
    private final AbilityBO abilityBO;

    public boolean calculateAndSaveCycle(List<DataForAbilityOfCycle> dataForAbilityOfCycleList, int userId){
        log.info("dataForAbilityOfCycleList {}", dataForAbilityOfCycleList);

        double max_avg_power = 100;

        for(DataForAbilityOfCycle dataForAbilityOfCycle : dataForAbilityOfCycleList){
            if( dataForAbilityOfCycle.getAverage_watts()!= null &&   dataForAbilityOfCycle.getAverage_watts() > max_avg_power){
                max_avg_power = dataForAbilityOfCycle.getAverage_watts();
            }
        }

        abilityBO.addAbility(max_avg_power,null,userId);
        return true; // 나중에 try catch 로 바꾼다
    }


}
