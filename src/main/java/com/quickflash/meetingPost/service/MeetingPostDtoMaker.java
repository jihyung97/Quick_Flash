package com.quickflash.meetingPost.service;

import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.BeforeMeetingDto;
import com.quickflash.meetingPost.dto.ReportMakingDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostDtoMaker {

    private final MeetingPostBO meetingPostBO;
    private final CommentBO commentBO;
    private final CommentService commentService;
    private final UserBO userBO;
    private final MeetingJoinDtoMaker meetingJoinDtoMaker;
    private final MeetingJoinBO meetingJoinBO;





    public List<ThumbnailDto> generateMeetingPostThumbnailDtoListForTest(){
        List<ThumbnailDto> thumbnailDtoList = new ArrayList<>();
        List<Map<String,Object>> parametersOfMeetingPostList = meetingPostBO.getMeetingPostForThumbnailListForTest();
        for(Map<String,Object> parametersOfMeetingPost : parametersOfMeetingPostList){
            Duration remainedTime = Duration.between((LocalDateTime)parametersOfMeetingPost.get("expiredAt"), LocalDateTime.now());
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
                    .remainedTime(remainedTime)
                    .build();
            thumbnailDtoList.add(thumbnailDto);
            //여기에 thumbnailDto 에 다른  domain 의 정보를 추가
            //thumbnailDto.setLeaderPace , setLeaderFtp, setLeaderName
        }

        return thumbnailDtoList;

    }


    public BeforeMeetingDto generateBeforeMeetingDto(int postId ){
        MeetingPost meetingPost = meetingPostBO.getMeetingPostById(postId) ;
        if(meetingPost == null){
            return null;
        }
        Duration duration = Duration.between(LocalDateTime.now(),meetingPost.getExpiredAt() );

        String remainedTimeStr;
        if (duration.isNegative()) {
            remainedTimeStr = "마감됨";
        } else {
            long hours = duration.toHours();
            long minutes = duration.minusHours(hours).toMinutes();
            remainedTimeStr = String.format("%02d시간 %02d분", hours, minutes);
        }


        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + remainedTimeStr);
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
                .joinList(meetingJoinDtoMaker.generateMeetingJoinBeforeMeetingDtoListByPostId(postId))
                .commentList(commentService.generateCommentDtoListByPostId(postId))
                .userName(userBO.getUserNameById(meetingPost.getUserId()))  // 필요 시
                .remainedTime(remainedTimeStr)
                .build();

        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!generateBeforeMeetingDTo");
        return beforeMeetingDto;

    }
    public ReportMakingDto generateReportMakingDto(int postId ){
        MeetingPost meetingPost = meetingPostBO.getMeetingPostById(postId) ;
        if(meetingPost == null){
            return null;
        }

        ReportMakingDto reportMakingDto = ReportMakingDto.builder()
                .postId(meetingPost.getId())
                .userId(meetingPost.getUserId())
                .title(meetingPost.getTitle())
                .location(meetingPost.getLocation())

                .contentText(meetingPost.getContentText())
                .exerciseType(meetingPost.getExerciseType())
                .distance(meetingPost.getDistance())
                .speed(meetingPost.getSpeed())
                .power(meetingPost.getPower())


                .createdAt(meetingPost.getCreatedAt())
                .updatedAt(meetingPost.getUpdatedAt())
                .meetingJoinList(meetingJoinDtoMaker.generateMeetingJoinReportMakingDtoListByPostId(postId))
                .imagePath(null)
                .userName(userBO.getUserNameById(meetingPost.getUserId()))  // 필요 시
                .expiredAt(meetingPost.getExpiredAt())

                .build();


        return reportMakingDto;

    }



}