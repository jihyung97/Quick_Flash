package com.quickflash.mileage.service;

import com.quickflash.meeting_join.dto.MeetingJoinDtoForBatch;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.mileage.domain.Mileage;
import com.quickflash.user.service.UserBO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@StepScope
@Component
@RequiredArgsConstructor
@Slf4j
public class MileageItemReader implements ItemReader<List<Mileage>> {

    private Integer standardId;


    private final MileageBO mileageBO;
//
//    @PostConstruct
//  public void  init(){
//         standardId = mileageBO.getMileageIdForDateStandard(LocalDate.now());
//    }



    private Integer startId;
    private final int batchSize = 10;

    @Override
    public List<Mileage> read() {

        if (standardId == null) {
            standardId = mileageBO.getMileageIdForDateStandard(LocalDate.now());
            if(standardId == null){
                return null;  // 오늘 기준이 되는 id가 없다는 건 오늘 저장된 mileage가 없다는 것이므로 종료
            }
        }

        log.info("selectedIdList {}"   );

        List<Mileage> selectedMileageList = mileageBO.getMileageForTrustBatch (startId,  batchSize );
        startId = selectedMileageList.get(selectedMileageList.size() - 1).getId;

        if(selectedMileageList == null || selectedMileageList.isEmpty()){
            return null;
        }
        //db에서 meetingJoin을 id 내림차순, 즉 최신 순부터 가져온다.
        // selectIdForDateStandard 로 1달 이전정도의 id를 가져와, 그 id보다 큰 값의 데이터를 가져온다. (id는 시간순이기 때문 )


        currentIndex += batchSize;



        return new ArrayList<>(selectedMileageList);
    }
}