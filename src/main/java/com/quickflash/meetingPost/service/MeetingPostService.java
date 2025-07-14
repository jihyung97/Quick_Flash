package com.quickflash.meetingPost.service;


import com.quickflash.ability.entity.AbilityEntity;
import com.quickflash.ability.service.AbilityBO;
import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.dto.IdAndScoreDto;
import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.dto.OneClickDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.trust.dto.TrustForOrderDto;
import com.quickflash.trust.service.TrustBO;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.calculation.CalculationService;
import com.quickflash.utility.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
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

    //cacheable로 저장 (key:userId,standardLat,standardLng,range) , double은 부동소수점이므로 반올림하여 안전하게 저장
//    @Cacheable(
//            key = "#userId + ':' + T(java.lang.Math).round(#standardLat * 10000) + ':' + T(java.lang.Math).round(#standardLng * 10000) + ':' + #range"
//    )

    public List<IdAndScoreDto> getIdAndScoreOrderedByScore( Integer userId, double standardLat,double standardLng , double range , LocalDateTime updatedAt){

        Double powerOfUser = 0.0;
        Double speedOfUser = 0.0;

        // 기준을 udpatedAt이 없으면 일주일전, 있으면 updatedAT으로 잡아 boundbox를 가져옴
        Integer standardId;
        if(updatedAt == null){
            standardId    = meetingPostBO.getPostIdForDateStandard( LocalDateTime.now().minusWeeks(1));
        }else{
            standardId = meetingPostBO.getPostIdForDateStandard(updatedAt);
        }

        //혹시 모를 NPE 방어
        if(standardId == null){
            standardId = 0;
        }


        Map<String,Object> latLngAndIdMap = calculationService.getLatLngForBoundBox(standardLat,standardLng,range);
        latLngAndIdMap.put("id", standardId);

        Map<Integer, MeetingPostForOrderDto> meetingPostMapByBoundBox = meetingPostBO.getPostIdsSelectedByBoundBoxAndIdForDate(latLngAndIdMap);   //updatedAt이 null이면 updatedAt을 일주일 전으로 설정, 있으면 이거보다 최근의 걸 가져온다
        if(userId != null){
            AbilityEntity abilityOfUser = abilityBO.getAbilityByUserId(userId);
            if(abilityOfUser != null){
                powerOfUser = abilityOfUser.getMaxCyclingAvgPower();
                speedOfUser = abilityOfUser.getMaxRunningAvgSpeed();

            }



        }

        log.info("meetingPostForORderDtoMapByBoundBox {}", meetingPostMapByBoundBox);

        Set<Integer> postKeySet =  meetingPostMapByBoundBox.keySet();

        List<Integer> userIdList = new ArrayList<>();

        //meetingPost userId의 keyset을 만든다
        for(int key :   postKeySet){
            userIdList.add(meetingPostMapByBoundBox.get(key).getUserId());
        }

        //keySet으로 trust의 정보를 가져온다 , Map<user, TrustForOrderDto
        Map<Integer, TrustForOrderDto> trustForOrderDtoMap = new HashMap<>();
    if(userIdList != null && !userIdList.isEmpty()){

        trustBO.getTrustForOrderDtoByMeetingByUserIdList(userIdList);
    }

      //  Map<Integer,Double> idToTotalScoreMap = new HashMap<>();
        //파워, 스피드에 대한

        List<IdAndScoreDto> idAndScoreDtoList = new ArrayList<>();

        for(int key : postKeySet){
            MeetingPostForOrderDto meetingPostForOrderDto = meetingPostMapByBoundBox.get(key);
            double distance = calculationService.calculateDistancesForMeetingPost(meetingPostForOrderDto.getLatitude(),meetingPostForOrderDto.getLongitude(),standardLat,standardLng);

            double trustOfMember = 0.0;
            Double powerScore = 0.0;
            Double speedScore = 0.0;
            TrustForOrderDto  trustForOrderDto = trustForOrderDtoMap.get(meetingPostForOrderDto.getUserId());
            if(trustForOrderDto != null){
                trustOfMember = trustForOrderDto .getTrustOfMember();

            }

            Double powerOfMeetingPost =  meetingPostForOrderDto.getPower();
            Double speedOfMeetingPost =  meetingPostForOrderDto.getSpeed();



            //운동 종류가 자전거이고 user의 파워 정보가 있을 때에만 점수를 낸다.

            double distanceScore = calculationService.calculateDistanceScore(distance);
            if(ExerciseType.CYCLE.name().equals(meetingPostForOrderDto.getExerciseType()) && powerOfUser != 0.0  ){
                if(powerOfUser > powerOfMeetingPost){
                   powerScore =  calculationService.calculatePowerScore(powerOfMeetingPost,powerOfUser);
                }else{
                   powerScore =  calculationService.calculatePowerScore(powerOfUser,powerOfMeetingPost);
                }

            }else if(ExerciseType.RUNNING.name().equals(meetingPostForOrderDto.getExerciseType()) && speedOfUser != 0.0 ){
                if(speedOfUser > speedOfMeetingPost){
                    speedScore =  calculationService.calculateSpeedScore(speedOfMeetingPost,speedOfUser);
                }else{
                    speedScore =  calculationService.calculateSpeedScore(speedOfUser,speedOfMeetingPost);
                }
            }

            double totalScore = calculationService.calculateTotalScore(powerScore,speedScore,distanceScore,trustOfMember);

            IdAndScoreDto idAndScoreDto = IdAndScoreDto.builder()
                    .id(key)
                    .score(totalScore)
                    .build();


            //powerOfUser, speedOfUser 은 이미 가져옴
            idAndScoreDtoList.add(idAndScoreDto);
        }
            //idToTOtalScoreMap에 있는 값을 totalScore 가 높은 순으로 정렬하고 반환
        log.info("idAndScoreDtoList {}", idAndScoreDtoList);

        List<IdAndScoreDto> orderedIdAndScoreDtoList = idAndScoreDtoList.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        return orderedIdAndScoreDtoList;


    }










}








