package com.quickflash.utility.validation;


import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ValidationService {
    private final MeetingPostBO meetingPostBO;
    public Map<String,Object> checkAndUpdateMeeting(LocalDateTime currentTime, int id){
        Map<String, Object> expiredAtAndStatus = meetingPostBO. getExpiredAtAndStatusOfMeetingById(id);
        LocalDateTime expiredAt = (LocalDateTime) expiredAtAndStatus.get("expiredAt");
        String statusOfMeeting = (String) expiredAtAndStatus.get("currentStatus");

        if (expiredAt == null || statusOfMeeting == null) {
            return null; // or throw exception
        }

        if(currentTime.isAfter(expiredAt)  && statusOfMeeting.equals(Status.BEFORE_MEETING.name())){
            meetingPostBO. updateStatusById(Status.REPORT_MAKING.name(), id);
            expiredAtAndStatus.put("currentStatus", Status.REPORT_MAKING.name());

        }



        return expiredAtAndStatus;

    }
}
