package com.quickflash.ComplexPage;

import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostDtoMaker;
import com.quickflash.meetingPost.service.MeetingPostService;
import com.quickflash.utility.calculation.CalculationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main-page")
@Slf4j
public class MainPageController {

    private final MeetingPostBO meetingPostBO;
    private final MeetingPostService meetingPostService;
    private final MeetingPostDtoMaker meetingPostDtoMaker;
    private final CalculationService calculationService;

    //localhost:8080/main-page/before-meeting
    @RequestMapping("/before-meeting")
    public String MainPageBeforeMeeting(
            HttpSession session
            , Model model
    ) {
        Integer userId = (Integer) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        String userLoginId = (String) session.getAttribute("userLoginId");
//        Double lat = (Double) session.getAttribute("lat");
//        Double lng = (Double) session.getAttribute("lng");




//            Map<Integer, MeetingPostForOrderDto> meetingPostMap = meetingPostBO.getPostIdsSelectedByBoundBox(calculationService.getLatLngForBoundBox(lat, lng, 10));
//            log.info("meetingPostByBoundBox At MainPagecontroller {}", meetingPostMap);
//            Set<Integer> keySet = meetingPostMap.keySet();
//            for (int key : keySet) {
//                MeetingPostForOrderDto meetingPost = meetingPostMap.get(key);
//                double distance = calculationService.calculateDistancesForMeetingPost(meetingPost.getLatitude(), meetingPost.getLongitude(), lat, lng);
//                log.info("distance Of meetingPost Id : {}  , {} 거리", key, distance);
//            }
//            log.info("CalculateScoreForMeetingPostOrder {}", meetingPostService.getPostIdsOrderByTotalScore(meetingPostMap, userId, lat, lng));


//            model.addAttribute("meetingPostList", meetingPostDtoMaker.generateMeetingPostThumbnailDtoListByScore(lat,lng,userId));


        //userId,userName,userLoginId
        if (userId != null && userName != null && userLoginId != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("userName", userName);
            userInfo.put("userLoginId", userLoginId);
            model.addAttribute("userInfo", userInfo);


        }

















        return "main_page/beforeMeeting";


    }
}


