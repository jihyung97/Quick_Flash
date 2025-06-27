package com.quickflash.ComplexPage;

import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main-page")
@Slf4j
public class MainPageController {

    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    //localhost:8080/main-page/before-meeting
    @RequestMapping("/before-meeting")
    public String MainPageBeforeMeeting(
            HttpSession session
          ,   Model model
    ){
        Integer userId = (Integer) session.getAttribute("userId");
        String userName = (String)session.getAttribute("userName");
        String userLoginId = (String)session.getAttribute("userLoginId");
        //userId,userName,userLoginId
        if(userId != null && userName != null && userLoginId != null){
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId",userId);
            userInfo.put("userName",userName);
            userInfo.put("userLoginId",userLoginId);
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("meetingPostList", meetingPostService.generateMeetingPostThumbnailDtoListForTest() );
            //log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + meetingPostService.generateMeetingPostThumbnailDtoListForTest().get(0).getTitle());
        }
        return "main_page/beforeMeeting";
    }

}
