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
            @RequestParam(value="postId", required = false)Integer postId
            , HttpSession session
            , Model model

            ){

        Integer sessionId = (Integer) session.getAttribute("userId");

        if(sessionId == null) {
            return "redirect:/main-page/before-meeting";
        }
        //어디로 갈지 option 정함
       ViewOption option = meetingPostService.decideViewWhenGoToMakeMeeting(sessionId,postId, LocalDateTime.now());



        if (option == ViewOption.UPDATE_MakeMeeting_VIEW && postId != null) {
            model.addAttribute("meetingPost", meetingPostBO.getMeetingPostById(postId));
            model.addAttribute("isPostExist", true);
            return "meeting_post/makeMeeting";
        }else if(option == ViewOption.CREATE_MakeMeeting_VIEW){
            model.addAttribute("isPostExist",  false);
            return "meeting_post/makeMeeting";
        }else if(option == ViewOption.MAIN_PAGE_VIEW){
            return "redirect:/main-page/before-meeting";
        }else{
            return "redirect:/main-page/before-meeting";
        }





    }
    //localhost:8080/meeting-post/before-meeting
    @RequestMapping("/before-meeting")
    public String beforeMeeting(Model model, HttpSession session,
                                @RequestParam(value="postId") int postId)
    {
        Integer sessionId = (Integer) session.getAttribute("userId");

        if(sessionId == null) {
            return "redirect:/main-page/before-meeting";
        }
        ViewOption option =  meetingPostService.decideViewWhenMeetingPostClicked(sessionId,postId, LocalDateTime.now());
        if(!(option == ViewOption.BEFORE_MAKING_LEADER_VIEW || option == ViewOption.BEFORE_MAKING_MEMBER_VIEW || option == ViewOption.BEFORE_MAKING_NONE_VIEW)){
            return "redirect:/main-page/before-meeting";
        }

            model.addAttribute("meetingPost", meetingPostService.generateBeforeMeetingDto(postId));
        if(option == ViewOption.BEFORE_MAKING_LEADER_VIEW) {
            model.addAttribute("userType", "leader");
        }else if(option == ViewOption.BEFORE_MAKING_MEMBER_VIEW) {
            model.addAttribute("userType", "member");
         } else {
            model.addAttribute("userType", "none");
    }


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
