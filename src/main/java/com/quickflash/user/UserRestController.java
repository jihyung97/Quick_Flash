package com.quickflash.user;

import com.quickflash.user.service.UserBO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserRestController {
    private final UserBO userBO;

    @RequestMapping("/create")
    public Map<String,Object> CreateUser(
            @RequestParam("loginId") String loginId
             ,@RequestParam("password") String password
             ,@RequestParam("name") String name
             ,@RequestParam("defaultLocation") String defaultLocation
             ,HttpSession session
    ){
//        log.info(loginId);
//        log.info(password);
//        log.info(name);
//        log.info(defaultLocation);
        Integer userId = (Integer) session.getAttribute("userId");

        Map<String,Object> result = new HashMap<>();
        if(userId == null){
            result.put("code",  500);
            return result;
        }
        boolean isAdded = userBO.addUser(
                loginId
                ,password
                ,name
                ,defaultLocation
        );

        if(isAdded){
            result.put("code",200);

        }else{
            result.put("code",500);
        }

        return result;



    }

    @GetMapping("/is-id-duplicated")
    public Map<String,Object> isIdDuplicated(
            @RequestParam("loginId") String loginId
    ){
        Map<String,Object> result = new HashMap<>();
        boolean isDuplicated = userBO.isLoginIdDuplicated(loginId);
        result.put("is_id_duplicated",isDuplicated);
        result.put("code",200);
        return result;

    }



}
