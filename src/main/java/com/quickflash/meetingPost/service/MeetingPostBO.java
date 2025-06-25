package com.quickflash.meetingPost.service;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostBO {
    private final MeetingPostMapper meetingPostMapper;

    public boolean isUserLeader(int sessionId, int postId){
        return sessionId == meetingPostMapper.getUserIdById(postId);
    }

    public int addMeetingPost(
        MeetingPost meetingPost
    ){
        return meetingPostMapper.insertMeetingPost(meetingPost);
    }

    public int updateStatusById (String currentStatus, int id){

        return meetingPostMapper.updateStatusById(currentStatus, id);
    }

    public Map<String, Object> getExpiredAtAndStatusOfMeetingById(int id){
        return meetingPostMapper.selectExpiredAtAndStatusById(id);
    }

    public MeetingPost getMeetingPostById(int id){
        return meetingPostMapper.selectMeetingPostById(id);
    }



    public Map<String,Object> checkAndUpdateMeeting(LocalDateTime currentTime, int id){
        Map<String, Object> expiredAtAndStatus = getExpiredAtAndStatusOfMeetingById(id);
        LocalDateTime expiredAt = (LocalDateTime) expiredAtAndStatus.get("expiredAt");
        String statusOfMeeting = (String) expiredAtAndStatus.get("currentStatus");

       if (expiredAt == null || statusOfMeeting == null) {
           return null; // or throw exception
       }

        if(currentTime.isAfter(expiredAt)  && statusOfMeeting.equals("기록 전")){
            updateStatusById("기록페이지",id);

        }

        return expiredAtAndStatus;

   }
}
