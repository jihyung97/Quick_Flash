package com.quickflash.mileage.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickflash.meetingPost.service.ExerciseType;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Response;
import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.service.MeetingJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor

public class MileageService {

    private final MeetingJoinService meetingJoinService;
    private final MileageBO mileageBO;


    public void updateMileage(int postId, String userIdToJoinStatusJson, Double distance, int sessionId, String exerciseType) {


        log.info("distance 확인 {}", distance);
        if (!Qualification.UPDATE_OK_AFTER_MEETING.equals(meetingJoinService.isMeetingJoinUpdateOk(sessionId, postId))) {

            return;
        }
        log.info("update qualification 도 통과됨" );

        Map<Integer, String> userIdToJoinStatus = new HashMap<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            userIdToJoinStatus = mapper.readValue(
                    userIdToJoinStatusJson,
                    new TypeReference<Map<Integer, String>>() {
                    }
            );


        } catch (Exception e) {

        }
        //  쿼리가 최대인원수 이므로 in절로 합치진 않아도 될거 같다.
        Set<Integer> idSet = userIdToJoinStatus.keySet();
        for (int id : idSet) {


                if (MeetingJoinStatus.COMPLETED_MEETING.name().equals(userIdToJoinStatus.get(id))) {
                    log.info("completed_meeting이 userIdToJoinStatus와 같은거까진 확인 ");
                    if (ExerciseType.RUNNING.name().equals(exerciseType)) {

                        mileageBO.addOrUpdateMileage(id, 0.0, distance);
                        log.info("user : {} 마일리지 성공적으로 넣었습니다", id);
                    } else {

                        mileageBO.addOrUpdateMileage(id, distance, 0.0);
                    }
                }



            // meetingjoin에서 completed 되었을 경우에만 마일리지에 추가한다.

        }


        ;

    }
}
