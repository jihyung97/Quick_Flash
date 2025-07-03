package com.quickflash.trust.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrustItemWriter implements ItemWriter<Map<Integer, Double>> {

    private final TrustBO trustBO;
    private final int batchSize = 10;

    @Override
    public void write(Chunk<? extends Map<Integer, Double>> items) throws Exception {
        Map<Integer, Double> totalMap = new HashMap<>();

        for (Map<Integer, Double> item : items.getItems()) {
            totalMap.putAll(item);


        }
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!totalMap{}",totalMap);
        if(totalMap != null && !totalMap.isEmpty()){
            trustBO.updateTrustByBatch(totalMap);
        }
      //  trustBO.updateTrustByBatch(totalMap);
//        trustBO.updateTrustByBatch(totalMap);
//        //전체 list를 순회하며  batchsize에 도달할 때마다 update,  마지막 batchsize에 도달하지 못하는 건 나머지 갯수만큼 업데이트
//        for (Map<Integer, Double> item : items) {
//            totalMap.putAll(item);
//
//            // 일정 크기에 도달하면 DB에 저장
//            if (totalMap.size() >= batchSize) {
//                trustBO.updateTrustByBatch(new HashMap<>(totalMap));
//                totalMap.clear();
//            }
//        }
//
//        // 남은 데이터 처리
//        if (!totalMap.isEmpty()) {
//            trustBO.updateTrustByBatch(totalMap);
//        }
    }
}