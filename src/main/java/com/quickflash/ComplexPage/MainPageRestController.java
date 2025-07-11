package com.quickflash.ComplexPage;

import com.quickflash.meetingPost.dto.OneClickDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import com.quickflash.meetingPost.service.MeetingPostDtoMaker;
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
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main-page")
@Slf4j

public class MainPageRestController {
    private final MeetingJoinService meetingJoinService;
    private final MeetingJoinBO meetingJoinBO;
    private final MeetingPostDtoMaker meetingPostDtoMaker;
    @PostMapping("/set-location")
    public Map<String,Object> setLocation(HttpSession session,
                                                @RequestParam double lat,
                                                @RequestParam double lng
    ) {

        Map<String, Object> result = new HashMap<>();
        try {
            session.setAttribute("lat", lat);
            session.setAttribute("lng", lng);
            result.put("result", "success");
        } catch (Exception e) {
            result.put("result", "fail");
        }
        return result;
    }


    @PostMapping("/activate-one-click")
    public Map<String,Object> activateOneClick(HttpSession session,
                                          @RequestParam double lat,
                                          @RequestParam double lng,
                                          @RequestParam Integer distance,
                                          @RequestParam int user_ftp,
                                          @RequestParam Integer height


    ) {
        log.info("height in activate-one-click{}", height);
        log.info("distance in activate-one-click{}", distance);
        List<OneClickDto> oneClickDtoList = meetingPostDtoMaker.generateOneCLickDtoList(lat,lng,distance,user_ftp);
      List<ThumbnailDto> thumbnailDtoList = meetingPostDtoMaker.generateThumbNailListByOneClickDtoList(oneClickDtoList, height);

      Map<String, Object> result = new HashMap<>();
       result.put("thumbnailDtoList", thumbnailDtoList);
       result.put("result", "success");


        return result;
    }







    }

