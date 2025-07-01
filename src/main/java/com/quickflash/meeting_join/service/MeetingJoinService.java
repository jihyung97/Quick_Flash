package com.quickflash.meeting_join.service;

import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Status;
import com.quickflash.meeting_join.MeetingJoinRestController;
import com.quickflash.meeting_join.MeetingJoinStatus;
import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import com.quickflash.meeting_join.repository.MeetingJoinRepository;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j

public class MeetingJoinService {
    private final ValidationService validationService;
    private final MeetingPostBO meetingPostBO;
    private final MeetingJoinBO meetingJoinBO;
    private final UserBO userBO;
    private final MeetingJoinRepository meetingJoinRepository;

    //기능상으론 BO이지만 userBO를 userName을 가져오기 위해 참조해야 하므로 service에 둔다
    @Transactional
    public boolean addMeetingJoin(
            int userId,
            int postId,
            boolean isSafetyAgree
    ){

        String userName = userBO.getUserNameById(userId);

        log.info("userName + {}", userName);

            MeetingJoinEntity meetingJoinEntity = MeetingJoinEntity.builder()
                    .userId(userId)
                    .postId(postId)
                    .userName(userName)
                    .joinStatus(MeetingJoinStatus.BEFORE_MEETING.name())
                    .isSafetyAgree(isSafetyAgree)
                    .createdAt(LocalDateTime.now())

                    .build();
            meetingJoinRepository.save(meetingJoinEntity);
            return true;



    }

    public Qualification isMeetingJoinCreateOk(int sessionId, int postId){
       Map<String,Object > result =  validationService.checkAndUpdateMeeting(LocalDateTime.now(),postId);
       if(!meetingPostBO.isPostExist(postId)){
           return Qualification.POST_NOT_EXIST;
       }
       if(meetingPostBO.isUserLeader(sessionId,postId))  {
           return Qualification.I_AM_LEADER;
       }
       if(meetingJoinBO.isIMember(postId,sessionId)){
           return Qualification.I_AM_MEMBER;
        }
       if(!Status.BEFORE_MEETING.name().equals( result.get("currentStatus"))){
           return Qualification.TIME_PASSED;
       }
       //post의 최대가능 인원수가 찼으면 더 이상 참가 불가
       if(meetingJoinBO.countMember(postId) + 1 >= meetingPostBO.getMaxHeadCountById(postId)){
           return Qualification.MAX_HEADCOUNT;
       }
       return Qualification.CREATE_OK_MEETING_JOIN;

    }

    public Qualification isMeetingJoinDeleteOk(int sessionId, int postId){
        Map<String,Object > result =  validationService.checkAndUpdateMeeting(LocalDateTime.now(),postId);
        if(!meetingPostBO.isPostExist(postId)){
            return Qualification.POST_NOT_EXIST;
        }
        if(!meetingJoinBO.isIMember(postId,sessionId)){
            return Qualification.NOT_MEMBER ;
        }
        if(!Status.BEFORE_MEETING.equals( result.get("currentStatus"))){
            return Qualification.TIME_PASSED;
        }
        return Qualification.DELETE_OK_MEETING_JOIN;

    }

    //report_making에서는 meeting_join 의 user가 여러명, final_report에서는 한명을 패러미터로
    public Qualification isMeetingJoinUpdateOk(int sessionId, int postId ){
        Map<String,Object > result =  validationService.checkAndUpdateMeeting(LocalDateTime.now(),postId);

        //게시글이 없으면 업데이트 x
        if(!meetingPostBO.isPostExist(postId)){
            return Qualification.POST_NOT_EXIST;
        }
        //리더가 아니면 업데이트 권한 x
        if(!meetingPostBO.isUserLeader(sessionId,postId)){
            return Qualification.NOT_LEADER ;
        }

        LocalDateTime expiredAt = (LocalDateTime) result.get("expiredAt");
        log.info("isMeetingJoinUpdateOk expiredAt {} ",expiredAt);
        if (expiredAt == null) {
            return Qualification.ERROR_GO_TO_MAIN;
        }

        // 모임 종료 시점부터 현재까지 경과 시간 계산
        Duration durationSinceExpired = Duration.between(expiredAt, LocalDateTime.now());
        if (Status.REPORT_MAKING .name().equals(result.get("currentStatus")) || Status.FINAL_REPORT .name().equals(result.get("currentStatus"))) {
            if (durationSinceExpired.toSeconds() <= 10800) {   //모임후에 3시간내엔 리더가 meeting_join의 상태 변경 가능함
                return Qualification.UPDATE_OK_AFTER_MEETING;
            } else {
                return Qualification.TIME_PASSED;
            }

        }else{
            return Qualification.ERROR_GO_TO_MAIN;
        }

    }


}
