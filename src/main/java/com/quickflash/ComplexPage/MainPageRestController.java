package com.quickflash.ComplexPage;

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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main-page")
@Slf4j

public class MainPageRestController {
    private final MeetingJoinService meetingJoinService;
    private final MeetingJoinBO meetingJoinBO;

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






    }

