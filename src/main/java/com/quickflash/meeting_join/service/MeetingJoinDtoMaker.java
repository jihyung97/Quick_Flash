package com.quickflash.meeting_join.service;

import com.quickflash.ability.entity.AbilityEntity;
import com.quickflash.ability.service.AbilityBO;
import com.quickflash.meeting_join.dto.MeetingJoinDto;
import com.quickflash.utility.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingJoinDtoMaker {
    private final MeetingJoinBO meetingJoinBO;
    private final ValidationService validationService;
    private final AbilityBO abilityBO;

   public List<MeetingJoinDto> generateMeetingJoinBeforeMeetingDtoListByPostId(int postId){

       List<Map<String,Object>> joinMapList =  meetingJoinBO.getMeetingJoinListForDtoByPostId(postId);
       log.info("joinMapList at generatemeetingjoinDto {}", joinMapList);
       Set<Integer> userIdSet = new HashSet<>();
       List<MeetingJoinDto>  meetingJoinDtoList = new ArrayList<>();


       for(Map<String,Object> joinMap : joinMapList){
           AbilityEntity abilityEntity = new AbilityEntity();
           Integer userId =  (Integer)joinMap.get("userId");
           if(userId != null){
               abilityEntity = abilityBO.getAbilityByUserId(userId);
           }
         Double power = 0.0;
           Double speed =0.0;
           if(abilityEntity != null){
               power = abilityEntity.getMaxCyclingAvgPower() == null? 0.0 :  abilityEntity.getMaxCyclingAvgPower();
               speed = abilityEntity.getMaxRunningAvgSpeed() == null? 0.0 :  abilityEntity.getMaxRunningAvgSpeed();
           }

           MeetingJoinDto meetingJoinDto = MeetingJoinDto.builder()

                   .userId(userId)
                   .postId((Integer)joinMap.get("postId") == null ? -1 : (Integer)joinMap.get("postId"))
                   .userName((String)joinMap.get("userName"))
                   .speed(speed)
                   .power(power)
                   .     build();
           meetingJoinDtoList.add(meetingJoinDto);
       }
       log.info("meetingJoinDtoList {}", meetingJoinDtoList);
       return meetingJoinDtoList;
   }

    public List<MeetingJoinDto> generateMeetingJoinReportMakingDtoListByPostId(int postId){

       List<MeetingJoinDto>  meetingJoinDtoList = new ArrayList<>();
        List<Map<String,Object>> joinMapList =  meetingJoinBO.getMeetingJoinListForDtoByPostId(postId);

        for(Map<String,Object> joinMap : joinMapList){
            Timestamp timestamp = (Timestamp) joinMap.get("createdAt");
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            MeetingJoinDto meetingJoinDto = MeetingJoinDto.builder()
                    .createdAt( timestamp.toLocalDateTime())
                                    .userId((int)joinMap.get("userId"))
                                            .postId((int)joinMap.get("postId"))
                                                    .userName((String)joinMap.get("userName"))

                    .     build();
           meetingJoinDtoList.add(meetingJoinDto);
        }

        return meetingJoinDtoList;
    }





}
