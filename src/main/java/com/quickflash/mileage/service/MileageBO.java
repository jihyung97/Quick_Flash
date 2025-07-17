package com.quickflash.mileage.service;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import com.quickflash.meetingPost.repository.MeetingPostRepository;
import com.quickflash.mileage.domain.Mileage;
import com.quickflash.mileage.mapper.MileageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MileageBO {
    private final MeetingPostMapper meetingPostMapper;
    private final MeetingPostRepository meetingPostRepository;
    private final MileageMapper mileageMapper;
    public void addOrUpdateMileage(int userId, Double mileageOfCycle, Double mileageOfRunning){
        log.info("여기까지 들어온거 확인");

        mileageOfCycle = mileageOfCycle == null ? 0.0 : mileageOfCycle;
        mileageOfRunning = mileageOfRunning == null ? 0.0 : mileageOfRunning;
        Mileage mileage = mileageMapper.selectLatestMileageByUserId(userId );
        log.info("mileageMapper 갔다옴");
        boolean isTodayMileageExist ;
        if(mileage == null){
            isTodayMileageExist = false;
        }else{
            if(LocalDate.now().equals(mileage.getCreatedAt())) isTodayMileageExist = true;
            else isTodayMileageExist = false;
        }
    log.info("isTodayMileageExist : {}", isTodayMileageExist);

        double previousCycle = isTodayMileageExist ? mileage.getMileageOfCycle() : 0.0;
        double previousRunning = isTodayMileageExist ? mileage.getMileageOfRunning() : 0.0;
          double  updatedCycle = previousCycle + mileageOfCycle;
           double updatedRunning = previousRunning + mileageOfRunning;

        Mileage newMileage = Mileage.builder()
                .userId(userId)
                .mileageOfRunning(updatedRunning)
                .mileageOfCycle(updatedCycle)
                .createdAt(LocalDate.now())
                .build();
        log.info("newMileage : {}", newMileage);
        if(isTodayMileageExist){
            mileageMapper.updateMileage(newMileage);
        }else{
            mileageMapper.insertMileage(newMileage);
        }



    }




    public List<Mileage> getMileageForTrustBatch(int startId , int batchSize) {
        log.info("");
        List<Mileage> batchList = mileageMapper.selectMileageForBatch( startId, batchSize);
        log.info("{}",batchList);
        return batchList;
    }
    public Integer getMileageIdForDateStandard(LocalDate date){
        return mileageMapper.selectMileageIdForDateStandard(date);
    }
    ;
}

