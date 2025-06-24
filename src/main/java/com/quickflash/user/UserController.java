package com.quickflash.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

}
