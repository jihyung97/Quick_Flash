package com.quickflash.meeting_join;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickflash.meetingPost.repository.MeetingPostRepository;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostService;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Response;
import com.quickflash.meeting_join.service.MeetingJoinBO;
import com.quickflash.meeting_join.service.MeetingJoinService;
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
@RequestMapping("/meeting-join")
@Slf4j

public class MeetingJoinRestController {
    private final MeetingJoinService meetingJoinService;
    private final MeetingJoinBO meetingJoinBO;

    @PostMapping("/create")
    public Map<String,Object> createMeetingJoin(HttpSession session,
                                                @RequestParam int postId,


                                                @RequestParam boolean isSafetyAgree
                                                ){

        Map<String,Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result", Response.FAILED.name());
            return result;
        }
        Qualification qualification = meetingJoinService.isMeetingJoinCreateOk(sessionId,postId);
        log.info("qualification + {}", qualification);
        if(!Qualification.CREATE_OK_MEETING_JOIN.equals(qualification) ){
            result.put("result", Response.FAILED);
            return result;
        }
        try{

            meetingJoinService.addMeetingJoin(sessionId,postId ,isSafetyAgree);
            result.put("result", Response.RESULT_OK.name());
        }catch(Exception e ){
            result.put("result", Response.FAILED.name());
        }
        return result;





    }

    @PostMapping("/delete")
    public Map<String,Object> deleteMeetingJoin(HttpSession session,
                                                @RequestParam int postId,
                                                @RequestParam  int userId

    ){
        Map<String,Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result", Response.FAILED.name());
            return result;
        }

        if(!Qualification.DELETE_OK_MEETING_JOIN.equals(meetingJoinService.isMeetingJoinDeleteOk(userId,postId)) ){
            result.put("result", Response.FAILED);
            return result;
        }
        try{

           meetingJoinBO.deleteMeetingJoin(userId,postId );
            result.put("result", Response.RESULT_OK);
        }catch(Exception e ){
            result.put("result", Response.FAILED);
        }
        return result;





    }

    @PostMapping("/update")
    public Map<String,Object> udpateMeetingJoin(HttpSession session,
                                                @RequestParam int postId,
                                                @RequestParam("userIdToJoinStatus") String userIdToJoinStatusJson


    ){

        Map<String,Object> result = new HashMap<>();

        Integer sessionId = (Integer) session.getAttribute("userId");

        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result", Response.FAILED.name());
            return result;
        }
        //meetingJoin 유효성 검사가 되지않으면 result는 fail
        if(!Qualification.UPDATE_OK_AFTER_MEETING .equals(meetingJoinService.isMeetingJoinUpdateOk(sessionId,postId)) ){
            result.put("result", Response.FAILED);
            return result;
        }
        log.info("updateMeetingJoin {}", meetingJoinService.isMeetingJoinUpdateOk(sessionId,postId));
        try{

            ObjectMapper mapper = new ObjectMapper();
            Map<Integer, String> userIdToJoinStatus  ;


                userIdToJoinStatus = mapper.readValue(
                        userIdToJoinStatusJson,
                        new TypeReference<Map<Integer, String>>() {}
                );


            meetingJoinBO.updateMeetingJoinStatusByPostIdAndUserIdList(postId,userIdToJoinStatus);
            result.put("result", Response.RESULT_OK);
        }catch(Exception e ){
            result.put("result", Response.FAILED);
        }
        return result;





    }

}
