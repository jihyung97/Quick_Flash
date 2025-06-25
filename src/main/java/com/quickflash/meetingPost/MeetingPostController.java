package com.quickflash.meetingPost;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostService;
import com.quickflash.meetingPost.service.ViewOption;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meeting-post")
public class MeetingPostController {
    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    //localhost:8080/meeting-post/make-meeting
    @RequestMapping("/make-meeting")
    public String makeMeeting(
            @RequestParam("postId")Integer postId
            , HttpSession session
            , Model model

            ){

        Integer sessionId = (Integer) session.getAttribute("userId");

        if(sessionId == null) {
            return "redirect:/main-page/before-meeting";
        }
        //어디로 갈지 option 정함
       ViewOption option = meetingPostService.decideViewMakeWhenMeetingClicked(sessionId,postId, LocalDateTime.now());



        if (option == ViewOption.UPDATE_MakeMeeting && postId != null) {
            model.addAttribute("meetingPost", meetingPostBO.getMeetingPostById(postId));
            return "meeting_post/make-meeting";
        }else if(option == ViewOption.CREATE_MakeMeeting){
            return "meeting_post/make-meeting";
        }else if(option == ViewOption.MAIN_PAGE){
            return "redirect:/main-page/before-meeting";
        }else{
            return "redirect:/main-page/before-meeting";
        }





    }
    //localhost:8080/meeting-post/before-meeting
    @RequestMapping("/before-meeting")
    public String beforeMeeting(){
        return "meeting_post/beforeMeeting";
    }

    //localhost:8080/meeting-post/final-report
    @RequestMapping("/final-report")
    public String finalReport(){
        return "meeting_post/finalReport";
    }
    //localhost:8080/meeting-post/report-making
    @RequestMapping("/report-making")
    public String reportMaking(){
        return "meeting_post/reportMaking";
    }
}
