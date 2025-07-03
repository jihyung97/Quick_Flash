package com.quickflash.trust.service;

import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.trust.domain.Trust;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TrustItemProcessor implements ItemProcessor<List<MeetingJoinDtoForBatch>, Map<Integer,Double>> {

    private final int user_size = 10;
    @Override
    public Map<Integer,Double> process(List<MeetingJoinDtoForBatch> meetingJoinList) throws Exception {
       //meetingJoinList를 meetingJoinMap으로 바꾸는 과정

        log.info("meetingJoinListmeetingJoinListmeetingJoinListmeetingJoinListmeetingJoinListmeetingJoinListmeetingJoinList{}",meetingJoinList);
        Map<Integer,Integer> totalCountMap = new HashMap<>();
        Map<Integer,Integer> joinCompletedCountMap = new HashMap<>();

        //순회하면서 유저별로 참가 완료한 갯수의  map을 가져온다.
        for(MeetingJoinDtoForBatch meetingJoin : meetingJoinList){
            int userId = meetingJoin.getUserId();
            totalCountMap.put(userId,  totalCountMap.getOrDefault(userId, 0) + 1);
            if(MeetingJoinStatus.COMPLETED_MEETING.name().equals(meetingJoin.getJoinStatus()) && totalCountMap.get(userId) <= 10){
                joinCompletedCountMap.put(userId, joinCompletedCountMap.getOrDefault(userId, 0) + 1);
            }
        }
//        Map<Integer,List<MeetingJoin>> meetingJoinMap = new HashMap<>();
//        Map<Integer,Double> trustOfMemberMap = new HashMap<>();
//        for (MeetingJoin meetingJoin : meetingJoinList) {
//            int userId = meetingJoin.getUserId();
//            meetingJoinMap.computeIfAbsent(userId, k -> new ArrayList<>())
//                    .add(meetingJoin);
//        }
//
//        Set<Integer> keySet = meetingJoinMap.keySet();
//        for (int key : keySet){
//
//         trustOfMemberMap.put(key, calculateTrustScore(meetingJoinMap.get(key)  )) ;
//
//
//        }

      return calculateTrustScore(joinCompletedCountMap,totalCountMap);
    }

    private  Map<Integer,Double>  calculateTrustScore(Map<Integer,Integer> joinCompletedCountMap,  Map<Integer,Integer> totalCountMap ) {

        Set<Integer> keySet = totalCountMap.keySet();
        Map<Integer,Double> trustMap = new HashMap<>();
        for(int key : keySet){
            int completedCount = joinCompletedCountMap.getOrDefault(key, 0);
            int totalCount = totalCountMap.getOrDefault(key, 0);
            if (totalCount > 0) {
                trustMap.put(key, (double) completedCount / totalCount);
            } else {
                trustMap.put(key, 0.0); // 또는 생략/예외 처리 등 전략 선택
            }



        }
        log.info("Calculated trustMap:@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {}", trustMap);
        return trustMap;


    }
}