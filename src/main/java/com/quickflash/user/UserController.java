package com.quickflash.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
