package com.quickflash.mileage.service;

import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.mileage.domain.Mileage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@StepScope
@Component
@Slf4j
@RequiredArgsConstructor
public class MileageItemProcessor implements ItemProcessor<List<Mileage>, Map<String,Object>> {


   private final RedisTemplate redisTemplate;



    @Override
    public Map<String,Object> process(List<Mileage> mileageList) throws Exception {
        //meetingJoinList를 meetingJoinMap으로 바꾸는 과정

        Map<String, Object> dailyRankingMap = redisTemplate.opsForHash().entries("daily:mileage:ranking");
        if(dailyRankingMap.isEmpty()){
            dailyRankingMap.put("bestIdOfCycle", 0);
            dailyRankingMap.put("bestScoreOfCycle", 0.0);
            dailyRankingMap.put("bestIdOfRunning", 0);
            dailyRankingMap.put("bestScoreOfRunning", 0.0);
        }

        //순회하면서 그리디하게 마일리지가 max인걸 찾는다
        for(Mileage mileage : mileageList){
            if(mileage.getMileageOfRunning() > (Double)dailyRankingMap.get("bestScoreOfRunning")) {
                dailyRankingMap.put("bestScoreOfRunning", mileage.getMileageOfRunning());
                dailyRankingMap.put("bestIdOfRunning", mileage.getUserId());
            }

            if(mileage.getMileageOfCycle() > (Double)dailyRankingMap.get("bestScoreOfCycle")) {
                dailyRankingMap.put("bestScoreOfCycle", mileage.getMileageOfCycle());
                dailyRankingMap.put("bestIdOfCycle", mileage.getUserId());
            }

        }

        log.info("dailyRankingMap {}", dailyRankingMap);
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

        redisTemplate.opsForHash().putAll("daily:mileage:ranking", dailyRankingMap);

        log.info("Redis에 랭킹 저장 완료");
        return null;
    }


}