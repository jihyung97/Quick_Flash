package com.quickflash.meeting_join.service;

import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.user.mapper.UserMapper;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@StepScope
@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingJoinItemReader implements ItemReader<List<MeetingJoinDtoForBatch>> {

    private final MeetingJoinMapper meetingJoinMapper;  // MyBatis Mapper
    private final UserBO userBO;

    private int currentIndex = 0;
    private final int batchSize = 10;

    @Override
    public List<MeetingJoinDtoForBatch> read() {
        log.info("selectedIdList {}"   );
         List<Integer> selectedIdList = userBO.getUserIdsForTrustBatch (currentIndex,  batchSize);

         log.info("selectedIdList {}" , selectedIdList);
         if(selectedIdList == null || selectedIdList.isEmpty()){
             return null;
         }
         //db에서 meetingJoin을 id 내림차순, 즉 최신 순부터 가져온다.
        // selectIdForDateStandard 로 1달 이전정도의 id를 가져와, 그 id보다 큰 값의 데이터를 가져온다. (id는 시간순이기 때문 )
        log.info("{}",LocalDateTime.now().minusMonths(1));
           List<MeetingJoinDtoForBatch> meetingJoinList   = meetingJoinMapper.selectMeetingJoinForTrustBatch(selectedIdList , meetingJoinMapper.selectIdForDateStandard(LocalDateTime.now().minusMonths(1)));

        currentIndex += batchSize;


        log.info("joinCompletedCountMap in Batch {}",meetingJoinList);
        return new ArrayList<>(meetingJoinList);
    }
}