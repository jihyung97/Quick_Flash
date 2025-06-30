package com.quickflash.meetingPost.service;

import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostViewDecider {


    private final MeetingPostBO meetingPostBO;
    private final CommentBO commentBO;
    private final CommentService commentService;
    private final UserBO userBO;
    private final MeetingJoinDtoMaker meetingJoinDtoMaker;
    private final MeetingJoinBO meetingJoinBO;
    private final ValidationService validationService;

    public ViewOption decideViewWhenGoToMakeMeeting(int sessionId, Integer postId, LocalDateTime currentTime){
        Map<String,Object> result = new HashMap<>();
        //미팅의 상태를 체크해서 상태가 모임전이고 지금 시간이 모임시작 시간보다 후이면 상태를 모임 페이지로 바꿈. 그 후 상태를 반환
        if(postId != null){
            result =  validationService .checkAndUpdateMeeting(currentTime,postId);
        }

        if(!result.isEmpty() && !(result.get("currentStatus").toString().equals(Status.BEFORE_MEETING.name())  ) ){
            return ViewOption.MAIN_PAGE_VIEW;
        }



        if(postId != null && meetingPostBO.isUserLeader(sessionId,postId)){
            //수정할 화면 보여준다


            return ViewOption.UPDATE_MakeMeeting_VIEW;
        }else{
            if(postId != null){
                return ViewOption.MAIN_PAGE_VIEW; // 잘못된 접근
            }else{
                //새로운 화면 보여줌

                return ViewOption.CREATE_MakeMeeting_VIEW;
            }
        }
    }
    public ViewOption decideViewWhenMeetingPostClicked(int sessionId, int postId, LocalDateTime currentTime){
        Map<String,Object> result = new HashMap<>();
        //미팅의 상태를 체크해서 상태가 모임전이고 지금 시간이 모임시작 시간보다 후이면 상태를 모임 페이지로 바꿈. 그 후 상태를 반환


        //checkAndUpdate 후 currentStatus가 2번으로 바뀌고 currentStatus가 2번으로 나와야 하는데 current
        result =   validationService.checkAndUpdateMeeting(currentTime,postId);
        boolean isLeader = meetingPostBO.isUserLeader(sessionId,postId);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@isLeader: {}", isLeader);
        boolean isJoin =  meetingJoinBO.isIMember(postId,sessionId);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@isJoin: {}", isJoin);
        String currentStatus = (String)result.get("currentStatus");

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@currentStatus: {}", currentStatus);
        if (Status.BEFORE_MEETING.name().equals(currentStatus)) {
            if (isLeader) {
                return ViewOption.BEFORE_MAKING_LEADER_VIEW;
            } else if (isJoin) {
                return ViewOption.BEFORE_MAKING_MEMBER_VIEW;
            } else {
                return ViewOption.BEFORE_MAKING_NONE_VIEW;
            }
        } else if (Status.REPORT_MAKING.name().equals(currentStatus)) {
            if (isLeader) {
                return ViewOption.REPORT_MAKING_VIEW;
            } else {
                return ViewOption.MAIN_PAGE_VIEW;
            }
        } else if (Status.FINAL_REPORT.name().equals(currentStatus)) {
            return ViewOption.FINAL_REPORT_VIEW;
        } else {
            return ViewOption.MAIN_PAGE_VIEW;
        }

    }

}
