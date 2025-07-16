package com.quickflash.mileage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Response;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinService;
import com.quickflash.mileage.service.MileageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileage")
@Slf4j

public class MileageRestController {
    private final MeetingJoinService meetingJoinService;
    private final MeetingJoinBO meetingJoinBO;
    private final MileageService mileageService;



        @PostMapping("/update")
     public Map<String,Object> updateMileage(
             @RequestParam("distance") String distance,
             @RequestParam("postId") int postId,
             @RequestParam("userIdToJoinStatus") String userIdToJoinStatusJson,
             @RequestParam("exerciseType") String exerciseType,
             HttpSession session

    ){


            Map<String,Object> result = new HashMap<>();

            Integer sessionId = (Integer) session.getAttribute("userId");

            // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
            if(sessionId == null) {
                result.put("result", Response.FAILED.name());
                return result;
            }
            try{
                mileageService.updateMileage(postId,userIdToJoinStatusJson,distance,sessionId,exerciseType);
                result.put("result","success");
                return result;
            } catch (Exception e) {
                result.put("result","fail");
                return result;
            }



    }

}