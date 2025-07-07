package com.quickflash.meetingPost.service;


import com.quickflash.api.strava.ability.entity.AbilityEntity;
import com.quickflash.api.strava.ability.service.AbilityBO;
import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.trust.dto.TrustForOrderDto;
import com.quickflash.trust.service.TrustBO;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.calculation.CalculationService;
import com.quickflash.utility.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final TrustBO trustBO;
    private final AbilityBO abilityBO;
    private final CalculationService calculationService;
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

    //power,speed, user와의 거리, trust 를 종합해서 점수를 계산하고 정렬 : bound-box 로 셀렉트된 Map<postId, Dto>
    public List<Integer> CalculateScoreForMeetingPostOrder(Map<Integer, MeetingPostForOrderDto> meetingPostForOrderDtoMap, int userId, double standardLat,double standardLng){

        AbilityEntity abilityOfUser = abilityBO.getAbilityByUserId(userId);
        Double powerOfUser = abilityOfUser.getMaxCyclingAvgPower();
        Double speedOFUser = abilityOfUser.getMaxRunningSpeed();


        Set<Integer> postKeySet =  meetingPostForOrderDtoMap.keySet();

        List<Integer> userIdList = new ArrayList<>();

        //meetingPost userId의 keyset을 만든다
        for(int key :   postKeySet){
            userIdList.add(meetingPostForOrderDtoMap.get(key).getUserId());
        }

        //keySet으로 trust의 정보를 가져온다 , Map<user, TrustForOrderDto
        Map<Integer, TrustForOrderDto> trustForOrderDtoMap =  trustBO.getTrustForOrderDtoByMeetingByUserIdList(userIdList);

        Map<Integer,Double> idToTotalScoreMap = new HashMap<>();
        //파워, 스피드에 대한

        for(int key : postKeySet){
            MeetingPostForOrderDto meetingPostForOrderDto = meetingPostForOrderDtoMap.get(key);
            double distance = calculationService.calculateDistancesForMeetingPost(meetingPostForOrderDto.getLatitude(),meetingPostForOrderDto.getLongitude(),standardLat,standardLng);
            double trustOfMember = trustForOrderDtoMap.get(meetingPostForOrderDto.getUserId()).getTrustOfMember();
            Double powerOfMeetingPost =  meetingPostForOrderDto.getPower();
            Double speedOfMeetingPost =  meetingPostForOrderDto.getSpeed();
            Double powerScore = 0.0;
            Double speedScore = 0.0;



            double distanceScore = calculationService.calculateDistanceScore(distance);
            if(ExerciseType.CYCLE.name().equals(meetingPostForOrderDto.getExerciseType()) && powerOfUser != 0.0 && powerOfUser != null){
                if(powerOfUser > powerOfMeetingPost){
                   powerScore =  calculationService.calculatePowerScore(powerOfMeetingPost,powerOfUser);
                }else{
                   powerScore =  calculationService.calculatePowerScore(powerOfUser,powerOfMeetingPost);
                }

            }else if(ExerciseType.RUNNING.name().equals(meetingPostForOrderDto.getExerciseType()) && speedOFUser != 0.0 && speedOFUser != null){
                if(speedOFUser > speedOfMeetingPost){
                    speedScore =  calculationService.calculateSpeedScore(speedOfMeetingPost,speedOFUser);
                }else{
                    speedScore =  calculationService.calculateSpeedScore(speedOFUser,speedOfMeetingPost);
                }
            }

            double totalScore = calculationService.calculateTotalScore(powerScore,speedScore,distanceScore,trustOfMember);

            idToTotalScoreMap.put(key, totalScore);


            //powerOfUser, speedOfUser 은 이미 가져옴

        }
            //idToTOtalScoreMap에 있는 값을 totalScore 가 높은 순으로 정렬하고 반환


        List<Integer> orderedMeetingPostList = idToTotalScoreMap.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // 내림차순 정렬
                .map(Map.Entry::getKey) // key (MeetingPost ID)만 추출
                .collect(Collectors.toList());

        return orderedMeetingPostList;

    }




}








