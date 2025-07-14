package com.quickflash.meetingPost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.service.CommentBO;
import com.quickflash.comment.service.CommentService;
import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.*;
import com.quickflash.meeting_join.dto.MeetingJoinDto;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinDtoMaker;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.calculation.CalculationService;
import com.quickflash.utility.time.TimeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final TimeService timeService;
    private final CalculationService calculationService;
    private final MeetingPostService meetingPostService;





    public List<ThumbnailDto> generateMeetingPostThumbnailDtoListForTest(){
        List<ThumbnailDto> thumbnailDtoList = new ArrayList<>();
        List<Map<String,Object>> parametersOfMeetingPostList = meetingPostBO.getMeetingPostForThumbnailListForTest();

        for(Map<String,Object> parametersOfMeetingPost : parametersOfMeetingPostList){

            //speed (km/h)를 페이스로 바꾼다 (min/kim)

            Map<String,Integer> pace = calculationService.convertspeedTopace((double)parametersOfMeetingPost.get("speed"));


            ThumbnailDto thumbnailDto = ThumbnailDto.builder()
                    .title((String)parametersOfMeetingPost.get("title"))
                    .isAbandonOkay((boolean)parametersOfMeetingPost.get("isAbandonOkay"))
                    .isRestExist((boolean)parametersOfMeetingPost.get("isRestExist"))
                    .minHeadCount((int)parametersOfMeetingPost.get("minHeadCount"))
                    .location((String)parametersOfMeetingPost.get("location"))
                    .power((double)parametersOfMeetingPost.get("power"))
                    .speed((double)parametersOfMeetingPost.get("speed"))
//                    .speed_min(pace.get("min"))
//                    .speed_sec(pace.get("sec"))
                    .distance((double)parametersOfMeetingPost.get("distance"))
                    .createdAt((LocalDateTime) parametersOfMeetingPost.get("createdAt"))
                    .expiredAt((LocalDateTime)parametersOfMeetingPost.get("expiredAt"))
                    .maxHeadCount((int)parametersOfMeetingPost.get("maxHeadCount"))
//                    .currentHeadCount(meetingJoinBO.countMember((int)parametersOfMeetingPost.get("id")) + 1)//리더까지 참여인원수에 포함
                    .id((int)parametersOfMeetingPost.get("id"))
                    .exerciseType((String)parametersOfMeetingPost.get("exerciseType"))
//                    .remainedTime(timeService.show_remainedTime(LocalDateTime.now(),(LocalDateTime)parametersOfMeetingPost.get("expiredAt")))
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

        Map<String,Integer> pace = calculationService.convertspeedTopace(meetingPost.getSpeed());
        List<MeetingJoinDto> joinDtoList = meetingJoinDtoMaker.generateMeetingJoinBeforeMeetingDtoListByPostId(postId);

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
                .speed_min(pace.get("min"))
                .speed_sec(pace.get("sec"))
                .power(meetingPost.getPower())
                .minHeadCount(meetingPost.getMinHeadCount())
                .maxHeadCount(meetingPost.getMaxHeadCount())
                .currentHeadCount(joinDtoList.size())
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
                .meetingJoinList(joinDtoList)
                .commentList(commentService.generateCommentDtoListByPostId(postId))
                .userName(userBO.getUserNameById(meetingPost.getUserId()))  // 필요 시
                .remainedTime(timeService.show_remainedTime(LocalDateTime.now(),meetingPost.getExpiredAt()))
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

    public FinalReportDto generateFinalReportDto(int postId ){
        MeetingPost meetingPost = meetingPostBO.getMeetingPostById(postId) ;
        if(meetingPost == null){
            return null;
        }

            // 모이전과 모임후의 댓글을 분리한다
        Map<String,List<CommentDto>> pairOfCommentList = commentService.
                seperateCommentDtoListByBeforeOrAfter(commentService.generateCommentDtoListByPostId(postId));

        FinalReportDto afterMeetingDto = FinalReportDto.builder()
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
                .meetingJoinList(meetingJoinDtoMaker.generateMeetingJoinBeforeMeetingDtoListByPostId(postId))
                .commentDtoListBeforeMeeting(pairOfCommentList.get("before"))
                .commentDtoListAfterMeeting(pairOfCommentList.get("after"))
                .userName(userBO.getUserNameById(meetingPost.getUserId()))  // 필요 시
                .remainedTime(timeService.show_remainedTime(LocalDateTime.now(),meetingPost.getExpiredAt()))
                .build();

        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!generateBeforeMeetingDTo");
        return afterMeetingDto;

    }
    @Cacheable(value = "shortCache", key = "'keyString:' + #sessionId + ':' + #lat + ':' + #lng")
    public List<ThumbnailDto> generateThumbnailDtoListByIdAndScoreDtos(List<IdAndScoreDto> idAndScoreDtoList){

        List<Integer> postIds = idAndScoreDtoList.stream()
                .map(IdAndScoreDto::getId)
                .collect(Collectors.toList());

        Map<Integer, Double> idToScoreMap = idAndScoreDtoList.stream()
                .collect(Collectors.toMap(
                        IdAndScoreDto::getId,     // key: id
                        IdAndScoreDto::getScore   // value: score
                ));


        List<ThumbnailDto> thumbnailDtoList = meetingPostBO.getThumbnailDtoListByPostIds(postIds);

        for(ThumbnailDto thumbnailDto : thumbnailDtoList){

            //speed (km/h)를 페이스로 바꾼다 (min/kim)

            Map<String,Integer> pace = calculationService.convertspeedTopace( thumbnailDto.getSpeed() );

            thumbnailDto.setSpeed_min(pace.get("min"));
            thumbnailDto.setSpeed_sec(pace.get("sec"));
            thumbnailDto.setCurrentHeadCount(meetingJoinBO.countMember(thumbnailDto.getId()) + 1);
            thumbnailDto.setRemainedTime(timeService.show_remainedTime(LocalDateTime.now(),(LocalDateTime)thumbnailDto.getExpiredAt()));


            Double score = idToScoreMap.get(thumbnailDto.getId());
            thumbnailDto.setScore(score != null ? score : 0.0);



        }

        return thumbnailDtoList;

    }
    public List<OneClickDto> generateOneCLickDtoList(double lat, double lng , double distance,int user_ftp){ // oneclick 설정에서 내 ftp설정할 수 있게한다. (ability테이블에서 안가져와도 된다)

        List<OneClickDto> oneClickDtoList = new ArrayList<>();
        List<Map<String,Object>> mapForOneClickList = meetingPostBO .getMapForOneClickByBoundBox(calculationService.getLatLngForBoundBox(lat, lng, distance));
        log.info("mapForOneClickList selected from getmapforoneclickbyboundbox {}", mapForOneClickList);
       for(Map<String,Object> mapForOneClick : mapForOneClickList){
          int id = (Integer)mapForOneClick.get("id");
           int userId  = (Integer)mapForOneClick.get("userId");
           Double power  = (Double)mapForOneClick.get("power");
           if(power == null || power == 0 || !ExerciseType.CYCLE.name().equals((String)mapForOneClick.get("exerciseType"))){ //파워가 없거나 0이면 oneClick 대상에 포함되지 않는다.
               continue;
           }
           Integer duration  = (Integer) mapForOneClick.get("duration"); // duration :
           if(duration == null || duration == 0){ //지속시간이 0이면 oneClick 대상에 포함되지 않는다.
               continue;
           }
           Integer height  =(Integer) mapForOneClick.get("height");  // 고도 : 10m 단위
           if(height == null  ){ // 고도가 없으면 0으로 치고 계산
              height = 0;
           }
           LocalDateTime expiredAt = (LocalDateTime) mapForOneClick.get("expiredAt");
           if(  expiredAt.isBefore(LocalDateTime.now())  ){ // 시작시간이 이미 지났으면  oneClick 대상 포함x
              continue;
           }
           long start_time = Duration.between(LocalDateTime.now(), expiredAt).toMinutes() / 10; //10분단위


            //파워가 있고 지속시간이 0이 아니며 고도는 default가 0이고 시작시간이 아직 안지났을 때 oneClickList에 추가

           // duration : 초단위, start_time : 10분단위
            OneClickDto oneClickDto = calculationService. calculateSstAndEndTime(power,duration,user_ftp,(int)start_time);
            if(oneClickDto.getEnd_time() >= 1000){
                continue;
            }
            oneClickDto.setId(id);
            oneClickDto.setHeight(height);
            oneClickDtoList.add(oneClickDto);
       }

       return oneClickDtoList;


    }

    public List<ThumbnailDto> generateThumbNailListByOneClickDtoList(List<OneClickDto> oneClickDtoList, int goal_height){
        List<Integer> postIds = calculationService.optimizeOneClick(oneClickDtoList, goal_height);

      return   meetingPostBO.getThumbnailDtoListByPostIds(postIds);

    }


    // 시간복잡도 : O(new log (new) + (old + new))
    public List<ThumbnailDto> mergeThumbnailDtoListByScore(List<ThumbnailDto> oldList, List<ThumbnailDto> newList) {

        if (oldList == null) {
            oldList = Collections.emptyList();  // 빈 리스트로 초기화
        }
        if (newList == null) {
            newList = Collections.emptyList();  // null 대비
        }

        // 1. newList 정렬 (내림차순)
        newList.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // 2. 병합 작업
        List<ThumbnailDto> result = new ArrayList<>(oldList.size() + newList.size());

        int i = 0, j = 0;
        while (i < oldList.size() && j < newList.size()) {
            if (oldList.get(i).getScore() >= newList.get(j).getScore()) {
                result.add(oldList.get(i++));
            } else {
                result.add(newList.get(j++));
            }
        }

        while (i < oldList.size()) {
            result.add(oldList.get(i++));
        }

        while (j < newList.size()) {
            result.add(newList.get(j++));
        }

        return result;
    }


    public List<ThumbnailDto> generateUpdatedThumbnailDto(
            Double lat,
            Double lng,
            LocalDateTime updatedAtOfthumbnailList,
             int sessionId,
            String oldThumbnailListjson


    ){
        Map<String,Object> result = new HashMap<>();
        List<ThumbnailDto > mergedThumbnailDtos = new ArrayList<>();


        final double range = 10.0; // 추후에 동적으로 설정할 수도 있음




        if(lat!= null && lng != null){
            //updatedAt 보다 최근의 걸 boundbox로 추려서 점수로 내어 postList로 가져옴.
            List<IdAndScoreDto> idAndScoreDtos =  meetingPostService. getIdAndScoreOrderedByScore(sessionId,lat,lng,range,updatedAtOfthumbnailList);
            List<ThumbnailDto> newThumbnailList=  generateThumbnailDtoListByIdAndScoreDtos(idAndScoreDtos);
            //oldthumbnailjson을 dtolist로 만든다

            List<ThumbnailDto> oldThumbnailList = new ArrayList<>();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                if(oldThumbnailListjson != null  && !oldThumbnailListjson.trim().isEmpty()){

                    oldThumbnailList = objectMapper.readValue(
                            oldThumbnailListjson,
                            new TypeReference<List<ThumbnailDto>>() {}
                    );
                }


                // 이후 로직...

            } catch (JsonProcessingException e) {
                // 예외 처리: 로그 출력, 에러 응답 반환 등
                e.printStackTrace();
            }


            // dtolist를 점수 내림차순으로 다시 정렬
            mergedThumbnailDtos =  mergeThumbnailDtoListByScore(oldThumbnailList, newThumbnailList);
            //result에 dtolist를 넣고 js에서 콜백


        }else{

        }// 세션에 저장된 좌표가 없으면 기존의 dtoList도 없앤다

        return mergedThumbnailDtos;

    }




}