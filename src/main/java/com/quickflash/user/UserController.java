package com.quickflash.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    //localhost:8080/user/sign-up
    @RequestMapping("/sign-up")
    public String signUp(){
        return "user/signUp";
    }

    @PostMapping("/sign-out")
    public String signOut(HttpSession session){
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userLoginId");

        return "redirect:/main-page/before-meeting";

    }
 //localhost:8080/user/kakao
    @RequestMapping("/kakao")
    public String isIdDuplicated(

    ){
       return "kakao/kakaoMap";

    }

}
