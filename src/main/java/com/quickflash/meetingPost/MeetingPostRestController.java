package com.quickflash.meetingPost;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.IdAndScoreDto;
import com.quickflash.meetingPost.dto.MeetingPostElasticIndexDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meetingPost.entity.MeetingPostEntity;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import com.quickflash.meetingPost.repository.MeetingPostRepository;
import com.quickflash.meetingPost.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-post")
@Slf4j
public class MeetingPostRestController {
    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    private final MeetingPostRepository meetingPostRepository;
    private final MeetingPostElasticSearchService meetingPostElasticSearchService;
    private final MeetingPostDtoMaker meetingPostDtoMaker;

    @PostMapping("/create")
    public Map<String, Object> createMeetingPost(
            HttpSession session,
            @RequestParam(value = "postId", required = false) Integer postId,
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String restLocation,
            @RequestParam LocalDateTime expiredAt, // String -> LocalDateTime 변환 필요
            @RequestParam(required = false) String contentText,
            @RequestParam(required = false) String afterMeetingContent,
            @RequestParam String exerciseType,
            @RequestParam Double distance,
            @RequestParam(required = false) Double speed,
            @RequestParam(required = false) Double power,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Integer height,
            @RequestParam Integer minHeadCount,
            @RequestParam Integer maxHeadCount,
            @RequestParam(required = false) Boolean isRestExist,
            @RequestParam Boolean isAbandonOkay,
            @RequestParam(required = false) Boolean isAfterPartyExist,
            @RequestParam(required = false) Boolean isLocationConnectedToKakao,
            @RequestParam(required = false) Boolean isUserAbilityConnectedToStrava,
            @RequestParam Boolean isMyPaceShown,
            @RequestParam Boolean isMyFtpShown

    ) {

        Map<String, Object> result = new HashMap<>();

        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if (sessionId == null) {
            result.put("result", Response.FAILED.name());
            return result;
        }
        Qualification qualification = meetingPostService.isMeetingPostCreateOk(postId, sessionId);
        log.info("Qualification + {}", qualification);
        if (Qualification.JOIN_ALREADY_EXISTS.equals(qualification)) {
            result.put("result", Response.FAILED.name());
            return result;

        } else if (Qualification.ERROR_GO_TO_MAIN.equals(qualification)) {
            result.put("result", Response.FAILED.name());
            return result;
        }
        //여기에 더 추가되는 거 있으면 추가......... 나중에는 response의 enum도 경우에 따라 다 따짐


        try {

            MeetingPost meetingPost = MeetingPost.builder()
                    .userId(sessionId)
                    .title(title)
                    .location(location)
                    .latitude(latitude != null ? latitude : 0.0)  // null이면 안되기 때문에 설정이 안되어 있으면 기본값 0,0 을 넣는다
                    .longitude(longitude != null ? longitude : 0.0)
                    .restLocation(restLocation)
                    .expiredAt(expiredAt) // 형식 처리 필요할듯
                    .contentText(contentText)
                    .afterMeetingContent(afterMeetingContent)
                    .exerciseType(exerciseType)
                    .distance(distance)
                    .speed(speed != null ? speed : 0)
                    .power(power != null ? power : 0)
                    .minHeadCount(minHeadCount != null ? minHeadCount : 0)
                    .maxHeadCount(maxHeadCount != null ? maxHeadCount : 10)
                    .isRestExist(isRestExist != null ? isRestExist : false)
                    .isAbandonOkay(isAbandonOkay != null ? isAbandonOkay : false)
                    .isAfterPartyExist(isAfterPartyExist != null ? isAfterPartyExist : false)
                    .isLocationConnectedToKakao(isLocationConnectedToKakao != null ? isLocationConnectedToKakao : false)
                    .isUserAbilityConnectedToStrava(isUserAbilityConnectedToStrava != null ? isUserAbilityConnectedToStrava : false)
                    .duration(duration)
                    .height(height)
                    .isMyPaceShown(isMyPaceShown != null ? isMyPaceShown : false)
                    .isMyFtpShown(isMyFtpShown != null ? isMyFtpShown : false)
                    .currentStatus(Status.BEFORE_MEETING.name())
                    .build();
            meetingPostBO.addMeetingPost(meetingPost);

            MeetingPostElasticIndexDto meetingPostElasticIndexDto = MeetingPostElasticIndexDto.builder()
                    .id(meetingPost.getId())
                    .title(meetingPost.getTitle())
                    .contentText(meetingPost.getContentText())
                    .afterMeetingContent(meetingPost.getAfterMeetingContent())
                    .exerciseType(meetingPost.getExerciseType())
                    .location(meetingPost.getLocation())
                    .latitude(meetingPost.getLatitude())
                    .longitude(meetingPost.getLongitude())
                    .distance(meetingPost.getDistance())
                    .speed(meetingPost.getSpeed())
                    .power(meetingPost.getPower())
                    .maxHeadCount(meetingPost.getMaxHeadCount())
                    .duration(meetingPost.getDuration())
                    .height(meetingPost.getHeight())
                    .currentStatus(meetingPost.getCurrentStatus())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .expiredAt(meetingPost.getExpiredAt())
                    .build();
            meetingPostElasticSearchService.indexMeetingPost(meetingPostElasticIndexDto);


            result.put("result", "성공");
        } catch (Exception e) {
            result.put("result", "작성 실패");
        }


        return result;
    }


    @PostMapping("/update")

    //formdata로 옴!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Map<String, Object> updateMeetingPost(
            HttpSession session,
            @RequestParam Integer postId,
            @RequestParam(required = false) String title,
            @RequestParam String location,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String restLocation,
            @RequestParam(required = false) LocalDateTime expiredAt,
            @RequestParam(required = false) String contentText,
            @RequestParam(required = false) String afterMeetingContent,
            @RequestParam String exerciseType,
            @RequestParam Double distance,
            @RequestParam(required = false) Double speed,
            @RequestParam(required = false) Double power,
            @RequestParam(required = false) Integer minHeadCount,
            @RequestParam(required = false) Integer maxHeadCount,
            @RequestParam(required = false) Boolean isRestExist,
            @RequestParam(required = false) Boolean isAbandonOkay,
            @RequestParam(required = false) Boolean isAfterPartyExist,
            @RequestParam(required = false) Boolean isLocationConnectedToKakao,
            @RequestParam(required = false) Boolean isUserAbilityConnectedToStrava,
            @RequestParam(required = false) Boolean isMyPaceShown,
            @RequestParam(required = false) Boolean isMyFtpShown
    ) {
        Map<String, Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");

        if (sessionId == null) {
            result.put("result", "로그인 안됨");
            return result;
        }


        Qualification option = meetingPostService.isMeetingPostUpdateOk(postId, sessionId);
        log.info(" update/create 업데이트 되도 되는지 + {}", option.name());
        if (!(option.equals(Qualification.UPDATE_OK_BEFORE_MEETING) || option.equals(Qualification.UPDATE_OK_AFTER_MEETING))) {
            result.put("result", Response.FAILED.name());
            return result;

        }

        if (option.equals(Qualification.UPDATE_OK_BEFORE_MEETING)) {
            meetingPostBO.updateMeetingPostWhenBeforeMeeting(
                    sessionId,
                    postId,
                    title,
                    location,
                    latitude,
                    longitude,
                    restLocation,
                    expiredAt,
                    contentText,


                    distance,
                    speed,
                    power,
                    minHeadCount,
                    maxHeadCount,
                    isRestExist,
                    isAbandonOkay,
                    isAfterPartyExist,
                    isLocationConnectedToKakao,
                    isUserAbilityConnectedToStrava,
                    isMyPaceShown,
                    isMyFtpShown

            );
        } else {
            log.info("updateMeetingPostWhenAfeterMeeting에서 업데이트 되고있나");
            meetingPostBO.updateMeetingPostWhenAfterMeeting(

                    sessionId,
                    postId,
                    location,
                    latitude,
                    longitude,
                    restLocation,
                    afterMeetingContent,
                    distance,
                    speed,
                    power,
                    isLocationConnectedToKakao,
                    isUserAbilityConnectedToStrava

            );

        }
        result.put("result", Response.RESULT_OK.name());
        log.info("업데이트 결과는 + {}", result.get("result"));
        return result;


        //여기에 더 추가되는 거 있으면 추가.........


    }

    @PostMapping("/search")
    public Map<String, Object> searchMeetingPost(
            @RequestParam("keyword") String keyword) {

        Map<String, Object> result = new HashMap<>();
        try {

            List<MeetingPostElasticIndexDto> searchList = meetingPostElasticSearchService.searchMeetingPost(keyword);

            result.put("searchList", searchList);
            result.put("result", "success");
        } catch (Exception e) {

            result.put("result", "fail");
        }

        return result;

    }

    @GetMapping("/thumbnail/update")
    public Map<String, Object> updateThumbnailDtoList(
            HttpSession session,
            LocalDateTime updatedAtOfthumbnailList, //local storage에서 가져옴
            String oldThumbnailListjson   // local storage에서 가져옴
            , String latAtStorage
            , String lngAtStorage


    ) {
        Map<String, Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");
        Double lat = null;
        Double lng = null;
        String latStr = (String) session.getAttribute("lat");
        String lngStr = (String) session.getAttribute("lng");

        //session에 등록된 좌표 (사용자 설정) 이 storage에 저장된 좌표(이전의 thumbnail update를 위해 가져왔던 좌표 ) 와 다르면 기존의 thumbnaildto 초기화
        if (!Objects.equals(latStr, latAtStorage) || !Objects.equals(lngStr, lngAtStorage)) {
            oldThumbnailListjson = null;
        }
        if (latStr != null && lngStr != null) {
            lat = Double.parseDouble(latStr);
            lng = Double.parseDouble(lngStr);
        }


        List<ThumbnailDto> thumbnailDtoList = meetingPostDtoMaker.generateUpdatedThumbnailDto(lat, lng, updatedAtOfthumbnailList, sessionId, oldThumbnailListjson);

        result.put("thumbnailDtoList", thumbnailDtoList);
        result.put("updatedAtOfthumbnailList",LocalDateTime.now());
        return result;

    }
}

