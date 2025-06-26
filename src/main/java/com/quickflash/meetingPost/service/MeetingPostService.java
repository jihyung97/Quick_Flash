package com.quickflash.meetingPost.service;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.ThumbnailDto;
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

    public ViewOption decideViewMakeWhenMeetingClicked(int sessionId, Integer postId, LocalDateTime currentTime){
        Map<String,Object> result = new HashMap<>();
       //미팅의 상태를 체크해서 상태가 모임전이고 지금 시간이 모임시작 시간보다 후이면 상태를 모임 페이지로 바꿈. 그 후 상태를 반환
        if(postId != null){
            result =   meetingPostBO.checkAndUpdateMeeting(currentTime,postId);
        }

            if(!result.isEmpty() && !result.get("currentStatus").equals("모임 전") ){
                return ViewOption.MAIN_PAGE;
            }



        if(postId != null && meetingPostBO.isUserLeader(sessionId,postId)){
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

    public List<ThumbnailDto> getMeetingPostThumbnailDtoForTest(){
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

}
