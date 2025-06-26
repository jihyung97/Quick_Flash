package com.quickflash.meetingPost;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import com.quickflash.meetingPost.service.MeetingPostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-post")
@Slf4j
public class MeetingPostRestController {
    private final MeetingPostBO meetingPostBO;

    @PostMapping("/create")
    public Map<String, Object> createMeetingPost(
            HttpSession session,
            @RequestParam(value = "postId", required = false) Integer postId,
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam (required = false)  Double latitude,
            @RequestParam (required = false)  Double longitude,
            @RequestParam(required = false) String restLocation,
            @RequestParam LocalDateTime expiredAt, // String -> LocalDateTime 변환 필요
            @RequestParam(required = false) String contentText,
            @RequestParam(required = false) String afterMeetingContent,
            @RequestParam String exerciseType,
            @RequestParam Double distance,
            @RequestParam(required = false) Double speed,
            @RequestParam(required = false) Double power,
            @RequestParam Integer minHeadCount,
            @RequestParam Integer maxHeadCount,
            @RequestParam (required = false) Boolean isRestExist,
            @RequestParam Boolean isAbandonOkay,
            @RequestParam (required = false) Boolean isAfterPartyExist,
            @RequestParam (required = false) Boolean isLocationConnectedToKakao,
            @RequestParam (required = false) Boolean isUserAbilityConnectedToStrava,
            @RequestParam Boolean isMyPaceShown,
            @RequestParam Boolean isMyFtpShown

    ) {
        Map<String, Object> result = new HashMap<>();

        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result", "로그인 안됨");
            return result;
        }


        if(postId != null){
            result.put("result", "잘못된 접근");
            return result;
        }

        LocalDateTime expiredDateTime;


        try{

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
                    .speed(speed)
                    .power(power)
                    .minHeadCount(minHeadCount)
                    .maxHeadCount(maxHeadCount)
                    .isRestExist(isRestExist != null ? isRestExist : false)
                    .isAbandonOkay(isAbandonOkay != null ? isAbandonOkay : false)
                    .isAfterPartyExist(isAfterPartyExist != null ? isAfterPartyExist : false)
                    .isLocationConnectedToKakao(isLocationConnectedToKakao != null ? isLocationConnectedToKakao : false)
                    .isUserAbilityConnectedToStrava(isUserAbilityConnectedToStrava != null ? isUserAbilityConnectedToStrava : false)

                    .isMyPaceShown(isMyPaceShown != null ? isMyPaceShown : false)
                    .isMyFtpShown(isMyFtpShown != null ? isMyFtpShown : false)
                    .currentStatus("모임 전")
                    .build();

            meetingPostBO.addMeetingPost(meetingPost);
            result.put("result", "성공");
        } catch (Exception e) {
            result.put("result", "작성 실패");
        }



        return result;
    }





}
