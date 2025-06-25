package com.quickflash.meetingPost;


import com.quickflash.meetingPost.service.MeetingPostBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meeting-post")
public class MeetingPostController {
    private final MeetingPostBO meetingPostBO;
    //localhost:8080/meeting-post/make-meeting
    @RequestMapping("/make-meeting")
    public String makeMeeting(
            @RequestParam("postId")int postId
            , HttpSession session
            , Model model

            ){

        Integer sessionId = (Integer) session.getAttribute("userId");

        if(sessionId == null) {
            return "redirect:/main-page/before-meeting";
        }

        if(meetingPostBO.isUserLeader(sessionId,postId)){
            meetingPostBO.get
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
