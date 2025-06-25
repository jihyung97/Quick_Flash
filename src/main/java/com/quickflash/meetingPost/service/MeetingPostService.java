package com.quickflash.meetingPost.service;


import com.quickflash.meetingPost.domain.MeetingPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostService {

    private final MeetingPostBO meetingPostBO;

    public ViewOption decideViewMakeWhenMeetingClicked(int sessionId, Integer postId, LocalDateTime currentTime){
        Map<String,Object> result =  meetingPostBO.checkAndUpdateMeeting(currentTime,postId);
        if(!result.get("currentStatus").equals("모임 전") ){
            return ViewOption.MAIN_PAGE;
        }

        if(meetingPostBO.isUserLeader(sessionId,postId)){
            //수정할 화면 보여준다


            return ViewOption.UPDATE_MakeMeeting;
        }else{
            if(postId != null){
                return ViewOption.MAIN_PAGE; // 잘못된 접근
            }else{
                //새로운 화면 보여줌

                return ViewOption.CREATE_MakeMeeting;
            }
        }
    }

}
