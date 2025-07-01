package com.quickflash.user;

import com.quickflash.user.entity.UserEntity;
import com.quickflash.user.service.UserBO;
import com.quickflash.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserRestController {
    private final UserBO userBO;
    private final UserService userService;

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
        if(userId != null){
            result.put("code",  500);
            return result;

        }
        userService. addAllTableRelatedToUser(
         userId,  loginId,   password,  name,   defaultLocation);





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

    @PostMapping("/sign-in")
    public Map<String,Object>  signIn(
            @RequestParam("loginId") String loginId
            , @RequestParam("password") String password
            , HttpSession session
    ){
        Map<String,Object> result = new HashMap<>();

        UserEntity user = userBO.getUserEntityByLoginIdAndPassword(loginId,password);
        log.info("User의 아이디" + user.getLoginId());
        log.info("User의 이름" + user.getName());
        if(user == null){
            result.put("code", 300);
            result.put("error_message", "존재하지 않는 사용자 입니다");
            return result;
        }
        //userId,userName,userLoginId
        session.setAttribute("userId", user.getId());


        session.setAttribute("userName", user.getName());
        session.setAttribute("userLoginId", user.getLoginId());
        result.put("code",200);
        return result;
    }




}
