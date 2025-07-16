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


        mileageOfCycle = mileageOfCycle == null ? 0.0 : mileageOfCycle;
        mileageOfRunning = mileageOfRunning == null ? 0.0 : mileageOfRunning;
        Mileage mileage = mileageMapper.selectMileageByUserIdAndCreatedAt(userId, LocalDate.now());

        double previousCycle = 0.0;
        double previousRunning = 0.0;

        if(mileage != null){
            previousCycle =  mileage.getMileageOfCycle() == null ? 0.0 : mileage.getMileageOfCycle();
            previousRunning =  mileage.getMileageOfRunning() == null ? 0.0 : mileage.getMileageOfRunning();

        }



          double  updatedCycle = previousCycle + mileageOfCycle;
           double updatedRunning = previousRunning + mileageOfRunning;

        Mileage newMileage = Mileage.builder()
                .userId(userId)
                .mileageOfRunning(updatedRunning)
                .mileageOfCycle(updatedCycle)
                .createdAt(LocalDate.now())
                .build();
        if(mileage != null){
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

