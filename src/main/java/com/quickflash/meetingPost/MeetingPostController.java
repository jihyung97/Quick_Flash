package com.quickflash.meetingPost;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meeting-post")
public class MeetingPostController {

    //localhost:8080/meeting-post/make-meeting
    @RequestMapping("/make-meeting")
    public String makeMeeting(){
        return "meeting_post/makeMeeting";
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
