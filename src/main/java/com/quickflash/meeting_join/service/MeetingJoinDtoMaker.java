package com.quickflash.meeting_join.service;

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

   public List<MeetingJoinDto> generateMeetingJoinBeforeMeetingDtoListByPostId(int postId){

       List<Map<String,Object>> joinMapList =  meetingJoinBO.getMeetingJoinListForDtoByPostId(postId);
       Set<Integer> userIdSet = new HashSet<>();
       List<MeetingJoinDto>  meetingJoinDtoList = new ArrayList<>();


       for(Map<String,Object> joinMap : joinMapList){
           MeetingJoinDto meetingJoinDto = MeetingJoinDto.builder()

                   .userId((int)joinMap.get("userId"))
                   .postId((int)joinMap.get("postId"))
                   .userName((String)joinMap.get("userName"))
                   .speed(5555)
                   .power(5555)
                   .     build();
           meetingJoinDtoList.add(meetingJoinDto);
       }
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
