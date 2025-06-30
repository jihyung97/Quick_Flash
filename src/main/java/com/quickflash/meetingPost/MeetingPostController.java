package com.quickflash.meetingPost;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MeetingPostController {
    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    private final MeetingPostViewDecider meetingPostViewDecider;
    private final MeetingPostDtoMaker meetingPostDtoMaker;

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
       ViewOption option = meetingPostViewDecider.decideViewWhenGoToMakeMeeting(sessionId,postId, LocalDateTime.now());
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + option.name());


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
        //sessionId 없으면 메인페이지로 튕김
        if(sessionId == null) {
            return "redirect:/main-page/before-meeting";
        }
        // before_makingview에서 리더로 보일지 멤버로 보일지 결정
        ViewOption option =  meetingPostViewDecider.decideViewWhenMeetingPostClicked(sessionId,postId, LocalDateTime.now());
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + option.name());
        // 클릭시 viewdecider에서 update되어 report_making으로 상태가 변했을 때 reportmaking페이지로 이동
        if( ViewOption.REPORT_MAKING_VIEW.equals(option)){
            return "redirect:/meeting-post/report-making?postId=" + postId;
        }
        //before_making view에 들어갈 조건이 아니라면 메인페이지로 감
        if(!(option == ViewOption.BEFORE_MAKING_LEADER_VIEW || option == ViewOption.BEFORE_MAKING_MEMBER_VIEW || option == ViewOption.BEFORE_MAKING_NONE_VIEW)){
            return "redirect:/main-page/before-meeting";
        }

            model.addAttribute("meetingPost", meetingPostDtoMaker.generateBeforeMeetingDto(postId));
        //리더인지, 멤버인지 구분하여 userType을 넣음. (브라우저에서는 수정버튼, 참가버튼의 유무의 차이정도)
        if(option == ViewOption.BEFORE_MAKING_LEADER_VIEW) {
            model.addAttribute("userType", "leader");
        }else if(option == ViewOption.BEFORE_MAKING_MEMBER_VIEW) {
            model.addAttribute("userType", "member");
         } else {
            model.addAttribute("userType", "none");
    }


        return "meeting_post/beforeMeeting";
    }




    //localhost:8080/meeting-post/report-making
    @RequestMapping("/report-making")
    public String reportMaking(Model model, HttpSession session,@RequestParam(value="postId") Integer postId) {
        Integer sessionId = (Integer) session.getAttribute("userId");

        if (sessionId == null || postId == null) {
            return "redirect:/main-page/before-meeting";
        }

        ViewOption option = meetingPostViewDecider.decideViewWhenMeetingPostClicked(sessionId, postId, LocalDateTime.now());
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + option.name());
        if (!(option == ViewOption.REPORT_MAKING_VIEW)) {
            return "redirect:/main-page/before-meeting";
        }
        log.info( "!!!!!!!!!!!!!!!!!!!!!!!!! postId는 +{}" ,meetingPostDtoMaker.generateReportMakingDto(postId).getPostId());
        model.addAttribute("meetingPost", meetingPostDtoMaker.generateReportMakingDto(postId));


        return "meeting_post/reportMaking";
    }



    //localhost:8080/meeting-post/final-report
    @RequestMapping("/final-report")
    public String finalReport(){
        return "meeting_post/finalReport";
    }






}
