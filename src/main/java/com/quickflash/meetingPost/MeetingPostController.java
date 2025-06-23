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
}
