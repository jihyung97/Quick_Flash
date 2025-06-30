package com.quickflash.meetingPost.service;


import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.validation.ValidationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostService {

    private final MeetingPostBO meetingPostBO;
    private final CommentBO commentBO;
    private final CommentService commentService;
    private final UserBO userBO;
    private final MeetingJoinDtoMaker meetingJoinDtoMaker;
    private final MeetingJoinBO meetingJoinBO;
    private final ValidationService validationService;


    public Qualification isMeetingPostCreateOk(Integer postId, int userId) {
        if (postId != null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }
        return Qualification.OK;
        //유저가 참가하거나 만든 미팅이 3개면 더이상 못만들게 하는 기능

    }

    public Qualification isMeetingPostDeleteOk(Integer postId, int userId) {
        if (postId == null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }
        if (!meetingPostBO.isPostExist(postId)) {
            return Qualification.POST_NOT_EXIST;
        }
        if (!meetingPostBO.isUserLeader(userId, postId)) {
            return Qualification.NOT_LEADER;
        }

        Map<String, Object> result = validationService.checkAndUpdateMeeting(LocalDateTime.now(), postId);
        LocalDateTime expiredAt = (LocalDateTime) result.get("expiredAt");

        if (expiredAt == null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }

        // 모임 종료 시점부터 현재까지 경과 시간 계산
        Duration durationSinceExpired = Duration.between(expiredAt, LocalDateTime.now());

        if (durationSinceExpired.toHours() >= -3) {
            return Qualification.AFTER_DELETE_TIME; // 삭제 제한 상태
        }

        return Qualification.OK;
    }

    public Qualification isMeetingPostUpdateOk(Integer postId, int userId) {
        if (postId == null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }
        if (!meetingPostBO.isPostExist(postId)) {
            return Qualification.POST_NOT_EXIST;
        }
        if (!meetingPostBO.isUserLeader(userId, postId)) {
            return Qualification.NOT_LEADER;
        }

        Map<String, Object> result = validationService.checkAndUpdateMeeting(LocalDateTime.now(), postId);
        LocalDateTime expiredAt = (LocalDateTime) result.get("expiredAt");

        if (expiredAt == null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }

        // 모임 종료 시점부터 현재까지 경과 시간 계산
        Duration durationSinceExpired = Duration.between(expiredAt, LocalDateTime.now());

        //모임 3시간 이전 부터는 업데이트 불가능 3600(s/h) * 3 (h)  = 10800초


        if (Status.BEFORE_MEETING.name().equals(result.get("currentStatus"))) {
            if (durationSinceExpired.toSeconds() >= -10800) {
                return Qualification.AFTER_UPDATE_TIME; // 모임전 3시간 부터 모임바로 전까지 제한
            } else {
                return Qualification.UPDATE_OK_BEFORE_MEETING;
            }

        } else if (Status.REPORT_MAKING.name().equals(result.get("currentStatus")) ) {
            //report_making일때는  3시간이 지나도 업데이트 가능하다
             return Qualification.UPDATE_OK_AFTER_MEETING;

        } else if(Status.FINAL_REPORT.name().equals(result.get("currentStatus"))){
            //모임 종료 후 3시간전까지는 업데이트 가능 (  final report일때)
            if (durationSinceExpired.toSeconds() <= 10800) {
                return Qualification.UPDATE_OK_AFTER_MEETING;
            } else {

                return Qualification.AFTER_UPDATE_TIME;
            }

        }

        return Qualification.ERROR_GO_TO_MAIN;


        //select는 ViewDecider에서 결정한다.... (CDU)는 only 리더만, R은 모두가....
    }


}








