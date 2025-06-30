package com.quickflash.utility.time;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class TimeService {

    public String show_remainedTime(LocalDateTime currentTime, LocalDateTime expiredAt){
        Duration remainedTime = Duration.between(LocalDateTime.now(),expiredAt );
        String remainedTimestr = "";
        if(remainedTime.isNegative()){
            remainedTimestr = "모임 종료";
        }else{
            long minutes = remainedTime.toMinutes();
            long day = minutes / 1440;
            long hour = (minutes % 1440) / 60;
            long minute = (minutes % 1440) % 60;
            remainedTimestr = day + "일" +hour  + "시간" + minute  + "분 남음";
        }
        return remainedTimestr;
    }


}
