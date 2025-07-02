package com.quickflash.meeting_join.service;

import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.user.mapper.UserMapper;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingJoinItemReader implements ItemReader<List<MeetingJoin>> {

    private final MeetingJoinMapper meetingJoinMapper;  // MyBatis Mapper
    private final UserBO userBO;

    private int currentIndex = 0;
    private final int batchSize = 10;

    @Override
    public List<MeetingJoin> read() {

         List<Integer> selectedIdList = userBO.getUserIdsForTrustBatch (currentIndex,  batchSize);
         log.info("selectedIdList {}" , selectedIdList);
           List<MeetingJoin> meetingJoinList   = meetingJoinMapper.selectMeetingJoinForTrustBatch(selectedIdList , LocalDateTime.now().minusMonths(1));
        currentIndex += batchSize;

        if (selectedIdList == null || selectedIdList.isEmpty()) {
            return null;  // 더 이상 데이터 없으면 null 반환 (배치 종료 신호)
        }
        log.info("meetingJoinList in Batch {}",meetingJoinList);
        return meetingJoinList;
    }
}