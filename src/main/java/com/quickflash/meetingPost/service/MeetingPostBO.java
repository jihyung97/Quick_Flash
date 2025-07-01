package com.quickflash.meetingPost.service;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meetingPost.entity.MeetingPostEntity;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import com.quickflash.meetingPost.repository.MeetingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingPostBO {
    private final MeetingPostMapper meetingPostMapper;
    private final MeetingPostRepository meetingPostRepository;


    public boolean isPostExist(int postId){
        return meetingPostRepository.existsById(postId);
    }
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

    public int getMaxHeadCountById(int id){return meetingPostMapper.selectMaxCountById(id);};




    public List<Map<String,Object>> getMeetingPostForThumbnailListForTest(){
       return meetingPostMapper.selectMeetingPostListForThumbnailTest();
    }


    //currentTime이 게시글의 모임시간보다 후이면 업데이트, 그 후 현재의 status return.


    public boolean updateMeetingPostWhenBeforeMeeting(


            int userId,
            Integer postId,
            String title,
            String location,
            Double latitude,
            Double longitude,
            String restLocation,
            LocalDateTime expiredAt,
            String contentText,


            Double distance,
            Double speed,
            Double power,
            Integer minHeadCount,
            Integer maxHeadCount,
            Boolean isRestExist,
            Boolean isAbandonOkay,
            Boolean isAfterPartyExist,
            Boolean isLocationConnectedToKakao,
            Boolean isUserAbilityConnectedToStrava,
            Boolean isMyPaceShown,
            Boolean isMyFtpShown
    ) {





        //before_meeting에서 업데이트 할때, report_making에서 업데이트할때 각각 다른 사항 분류


        //before_meeting 에서 업데이트 일때

        try {

            MeetingPost updatedMeetingPost = MeetingPost.builder()

                    .id(postId)
                    .title(title) //before_meeting일때만 업데이트 가능
                    .location(location) //둘다 가능함
                    .latitude(latitude != null ? latitude : 0.0) //둘다
                    .longitude(longitude != null ? longitude : 0.0) //둘다
                    .restLocation(restLocation) //둘다
                    .expiredAt(expiredAt) //모임전만
                    .contentText(contentText) //모임전만

                    .distance(distance) //둘다
                    .speed(speed != null ? speed : 0) //둘다
                    .power(power != null ? power : 0) //둘다
                    .minHeadCount(minHeadCount != null ? minHeadCount : 0) //전만
                    .maxHeadCount(maxHeadCount != null ? maxHeadCount : 10) //전만
                    .isRestExist(isRestExist != null ? isRestExist : false) //전만
                    .isAbandonOkay(isAbandonOkay != null ? isAbandonOkay : false) //전만
                    .isAfterPartyExist(isAfterPartyExist != null ? isAfterPartyExist : false) //전만
                    .isLocationConnectedToKakao(isLocationConnectedToKakao != null ? isLocationConnectedToKakao : false) //둘다
                    .isUserAbilityConnectedToStrava(isUserAbilityConnectedToStrava != null ? isUserAbilityConnectedToStrava : false) //둘다
                    .isMyPaceShown(isMyPaceShown != null ? isMyPaceShown : false) //전만
                    .isMyFtpShown(isMyFtpShown != null ? isMyFtpShown : false) //전만

                    .updatedAt(LocalDateTime.now()) // 수정일 업데이트
                    .build();

                meetingPostMapper.updateMeetingPostBeforeMeetingById(updatedMeetingPost );
                return true;

        }catch(Exception e){
            return false;
        }
    }

    public boolean updateMeetingPostWhenAfterMeeting(

            int userId,
            Integer postId,
            String location,
            Double latitude,
            Double longitude,
            String restLocation,
           String afterMeetingContent,
            Double distance,
            Double speed,
            Double power,
            Boolean isLocationConnectedToKakao,
            Boolean isUserAbilityConnectedToStrava

    ) {





        //before_meeting에서 업데이트 할때, report_making에서 업데이트할때 각각 다른 사항 분류


        //before_meeting 에서 업데이트 일때

        try {

            MeetingPost updatedMeetingPost = MeetingPost.builder()


                    .id(postId)
                    .location(location) //둘다 가능함
                    .latitude(latitude != null ? latitude : 0.0) //둘다
                    .longitude(longitude != null ? longitude : 0.0) //둘다
                    .restLocation(restLocation) //둘다

                    .afterMeetingContent(afterMeetingContent)
                    .distance(distance) //둘다
                    .speed(speed != null ? speed : 0) //둘다
                    .power(power != null ? power : 0) //둘다

                    .isLocationConnectedToKakao(isLocationConnectedToKakao != null ? isLocationConnectedToKakao : false) //둘다
                    .isUserAbilityConnectedToStrava(isUserAbilityConnectedToStrava != null ? isUserAbilityConnectedToStrava : false) //둘다
                    .currentStatus(Status.FINAL_REPORT.name())
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 모임후에 업데이트시엔 무조건 final_report로! (report_making에서  final_report로)
                    .updatedAt(LocalDateTime.now()) // 수정일 업데이트
                    .build();
            meetingPostMapper.updateMeetingPostAfterMeeting(updatedMeetingPost );
            return true;

            }catch(Exception e){
                return false;
            }




    }

}
