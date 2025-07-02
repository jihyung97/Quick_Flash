package com.quickflash.trust.service;

import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.trust.domain.Trust;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TrustItemProcessor implements ItemProcessor<List<MeetingJoin>, Map<Integer,Double>> {

    @Override
    public Map<Integer,Double> process(List<MeetingJoin> meetingJoinList) throws Exception {
       //meetingJoinList를 meetingJoinMap으로 바꾸는 과정

        Map<Integer,List<MeetingJoin>> meetingJoinMap = new HashMap<>();
        Map<Integer,Double> trustOfMemberMap = new HashMap<>();
        for (MeetingJoin meetingJoin : meetingJoinList) {
            int userId = meetingJoin.getUserId();
            meetingJoinMap.computeIfAbsent(userId, k -> new ArrayList<>())
                    .add(meetingJoin);
        }

        Set<Integer> keySet = meetingJoinMap.keySet();
        for (int key : keySet){

         trustOfMemberMap.put(key, calculateTrustScore(meetingJoinMap.get(key)  )) ;


        }

      return trustOfMemberMap;
    }

    private double calculateTrustScore(List<MeetingJoin> meetingJoinList) {

        int cnt = 0;
        for(MeetingJoin meetingJoin : meetingJoinList){
            if(MeetingJoinStatus.COMPLETED_MEETING.name().equals(meetingJoin.getJoinStatus()) ) {
                cnt++;
            }
        }
        return  (double)cnt / meetingJoinList.size();

    }
}