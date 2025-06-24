package com.quickflash.ComplexPage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main-page")
public class MainPageController {

    //localhost:8080/main-page/before-meeting
    @RequestMapping("/before-meeting")
    public String MainPageBeforeMeeting(){
        return "main_page/beforeMeeting";
    }

}
