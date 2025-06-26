package com.quickflash.meetingPost.service;


import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.join.service.JoinBO;
import com.quickflash.join.service.JoinService;
import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.BeforeMeetingDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostService {

    private final MeetingPostBO meetingPostBO;
    private final CommentBO commentBO;
    private final CommentService commentService;
    private final UserBO userBO;
    private final JoinService joinService;
    private final JoinBO joinBO;

    public ViewOption decideViewWhenGoToMakeMeeting(int sessionId, Integer postId, LocalDateTime currentTime){
        Map<String,Object> result = new HashMap<>();
       //미팅의 상태를 체크해서 상태가 모임전이고 지금 시간이 모임시작 시간보다 후이면 상태를 모임 페이지로 바꿈. 그 후 상태를 반환
        if(postId != null){
            result =   meetingPostBO.checkAndUpdateMeeting(currentTime,postId);
        }

            if(!result.isEmpty() && !(result.get("currentStatus").toString().equals("모임 전")  ) ){
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

            result =   meetingPostBO.checkAndUpdateMeeting(currentTime,postId);
            boolean isLeader = meetingPostBO.isUserLeader(sessionId,postId);
            boolean isJoin =  joinBO.isIMember(postId,sessionId);
            String currentStatus = (String)result.get("currentStatus");

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



    public List<ThumbnailDto> generateMeetingPostThumbnailDtoListForTest(){
        List<ThumbnailDto> thumbnailDtoList = new ArrayList<>();
        List<Map<String,Object>> parametersOfMeetingPostList = meetingPostBO.getMeetingPostForThumbnailListForTest();
       for(Map<String,Object> parametersOfMeetingPost : parametersOfMeetingPostList){

           ThumbnailDto thumbnailDto = ThumbnailDto.builder()
                   .title((String)parametersOfMeetingPost.get("title"))
                   .isAbandonOkay((boolean)parametersOfMeetingPost.get("isAbandonOkay"))
                   .isRestExist((boolean)parametersOfMeetingPost.get("isRestExist"))
                   .minHeadCount((int)parametersOfMeetingPost.get("minHeadCount"))
                   .location((String)parametersOfMeetingPost.get("location"))
                   .power((double)parametersOfMeetingPost.get("power"))
                   .speed((double)parametersOfMeetingPost.get("speed"))
                   .distance((double)parametersOfMeetingPost.get("distance"))
                   .createdAt((LocalDateTime) parametersOfMeetingPost.get("createdAt"))
                   .expiredAt((LocalDateTime)parametersOfMeetingPost.get("expiredAt"))
                   .maxHeadCount((int)parametersOfMeetingPost.get("maxHeadCount"))
                   .postId((int)parametersOfMeetingPost.get("id"))
                   .exerciseType((String)parametersOfMeetingPost.get("exerciseType"))
                   .build();
           thumbnailDtoList.add(thumbnailDto);
           //여기에 thumbnailDto 에 다른  domain 의 정보를 추가
           //thumbnailDto.setLeaderPace , setLeaderFtp, setLeaderName
       }

       return thumbnailDtoList;

    }


    public BeforeMeetingDto generateBeforeMeetingDto(int postId){
         MeetingPost meetingPost = meetingPostBO.getMeetingPostById(postId);

        BeforeMeetingDto beforeMeetingDto = BeforeMeetingDto.builder()
                .postId(meetingPost.getId())
                .userId(meetingPost.getUserId())
                .title(meetingPost.getTitle())
                .location(meetingPost.getLocation())
                .latitude(meetingPost.getLatitude())
                .longitude(meetingPost.getLongitude())
                .restLocation(meetingPost.getRestLocation())
                .expiredAt(meetingPost.getExpiredAt())
                .contentText(meetingPost.getContentText())
                .exerciseType(meetingPost.getExerciseType())
                .distance(meetingPost.getDistance())
                .speed(meetingPost.getSpeed())
                .power(meetingPost.getPower())
                .minHeadCount(meetingPost.getMinHeadCount())
                .maxHeadCount(meetingPost.getMaxHeadCount())
                .isRestExist(meetingPost.getIsRestExist())
                .isAbandonOkay(meetingPost.getIsAbandonOkay())
                .isAfterPartyExist(meetingPost.getIsAfterPartyExist())
                .isLocationConnectedToKakao(meetingPost.getIsLocationConnectedToKakao())
                .isUserAbilityConnectedToStrava(meetingPost.getIsUserAbilityConnectedToStrava())
                .isMyPaceShown(meetingPost.getIsMyPaceShown())
                .isMyFtpShown(meetingPost.getIsMyFtpShown())
                .currentStatus(meetingPost.getCurrentStatus())
                .createdAt(meetingPost.getCreatedAt())
                .updatedAt(meetingPost.getUpdatedAt())
                .joinList(joinService.generateJoinBeforeMeetingDtoListByPostId(postId))
                .commentList(commentService.generateCommentDtoListByPostId(postId))
                .userName(userBO.getUserNameById(meetingPost.getUserId()))  // 필요 시
                .build();


            return beforeMeetingDto;

    }

}
