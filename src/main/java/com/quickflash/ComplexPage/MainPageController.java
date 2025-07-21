package com.quickflash.ComplexPage;

import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.MeetingPostDtoMaker;
import com.quickflash.meetingPost.service.MeetingPostService;
import com.quickflash.user.service.UserBO;
import com.quickflash.utility.calculation.CalculationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate redisTemplate;
    private final UserBO userBO;

    //localhost:8080/main-page/before-meeting
    @RequestMapping("/before-meeting")
    public String MainPageBeforeMeeting(
            HttpSession session
            , Model model
    ) {
        Integer userId = (Integer) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        String userLoginId = (String) session.getAttribute("userLoginId");



        String latStr = (String) session.getAttribute("lat");
        String lngStr = (String) session.getAttribute("lng");

        double lat = 37.5665;
        double lng = 126.9780;

            if (latStr != null && !latStr.isEmpty())     lat = Double.parseDouble(latStr);
            if (lngStr != null && !lngStr.isEmpty())   lng = Double.parseDouble(lngStr);

            model.addAttribute("lat", lat);
            model.addAttribute("lng", lng);





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
            List<ThumbnailDto> myScheduleList = meetingPostDtoMaker.getMyScheduleThumbnailByUserId(userId);
            model.addAttribute("myScheduleList",myScheduleList );



        }

        Map<String, Object> dailyRanking = redisTemplate.opsForHash().entries("daily:mileage:ranking");
        if(dailyRanking != null && !dailyRanking.isEmpty()){

            Integer rankingIdOfCycle = (Integer)dailyRanking.get("bestIdOfCycle");
            Integer rankingIdOfRunning = (Integer)dailyRanking.get("bestIdOfRunning");
            String rankingNameOfCycle  = "";
            String rankingNameOfRunning = "";
            if(rankingIdOfCycle != null){
                rankingNameOfCycle = userBO.getUserNameById(rankingIdOfCycle);
            }


            if(rankingIdOfRunning != null){
                rankingNameOfRunning = userBO.getUserNameById(rankingIdOfRunning);
            }

            dailyRanking.put("rankingNameOfRunning", rankingNameOfRunning);
            dailyRanking.put("rankingNameOfCycle", rankingNameOfCycle);



            model.addAttribute("dailyRanking", dailyRanking);



        }






        return "main_page/beforeMeeting";


    }
}


