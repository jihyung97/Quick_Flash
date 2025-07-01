package com.quickflash.utility.calculation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CalculationService {
   public Map<String,Integer> convertspeedTopace(double speed  ){
       Map<String,Integer> speedMap = new HashMap<>();
        if(speed == 0 ){
            speedMap.put("min", 0);
            speedMap.put("sec", 0);
            return speedMap;
        }
        double pace = 60 / speed;
       int min = (int)pace;
       int sec = (int)((pace - min) * 60);
       speedMap.put("min", min);
       speedMap.put("sec", sec);
       return speedMap;

    }
    public double convertPaceToSpeed(int pace_min, int pace_sec  ){

       if(!(pace_min == 0 && pace_sec == 0)){
           return 3600 / (pace_min * 60 + pace_sec);
       }
       return 0;

    }
}
