package com.quickflash.meeting_join.service;

import com.quickflash.meeting_join.dto.MeetingJoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingJoinService {
    private final MeetingJoinBO meetingJoinBO;

   public List<MeetingJoinDto> generateMeetingJoinBeforeMeetingDtoListByPostId(int postId){

       List<MeetingJoinDto> meetingJoinBeforeList =  meetingJoinBO.getMeetingJoinListForDtoByPostId(postId);
       Set<Integer> userIdSet = new HashSet<>();


       for(MeetingJoinDto meetingJoinBefore : meetingJoinBeforeList){
           userIdSet.add(meetingJoinBefore.getUserId());
       }




     // ability table에서 userIdSet으로 UserIdToAbility Map 만들기
       for(MeetingJoinDto joinBeforeMeetingDto : meetingJoinBeforeList){

       }
       return meetingJoinBeforeList;
   }

    public List<MeetingJoinDto> generateMeetingJoinReportMakingDtoListByPostId(int postId){

        List<MeetingJoinDto> joinReportMakingList =  meetingJoinBO.getMeetingJoinListForDtoByPostId(postId);

        return joinReportMakingList;
    }



}
