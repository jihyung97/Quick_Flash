package com.quickflash.api.strava.OAuthClient;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class StravaOAuthController {

    private final StravaOAuthClient stravaOAuthClient;
    private final StravaTokenService stravaTokenService;

    @GetMapping("/connect/strava")
    public String connectStrava(HttpSession session) {
        //   로그인된 사용자 확인
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/main-page/before-meeting";
        }

         // authentication url로 이동
        String authorizeUrl = stravaOAuthClient.buildAuthorizeUrl( );
        return "redirect:" + authorizeUrl;
    }



    @GetMapping("/oauth/strava/callback")
    //여기서 code는 connectStrava에서 redirect하면서 받은 authentication code?
    //여기서 콜백 받으면 code를 보내 토큰을 받아온다. 그후 db에 저장
    public String handleCallback(@RequestParam("code") String code, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        try{

            // code로 access_token 교환
            StravaTokenResponse stravaTokenResponse = stravaOAuthClient.exchangeCodeForToken(code);

            // 토큰 저장 (DB or 세션)
            stravaTokenService.addStravaToken(userId, stravaTokenResponse);
            String endPoint = (String) session.getAttribute("connect-exercise");
            return "redirect:" + endPoint; // 다시 connect-cycle로 돌아감


        }catch(Exception e){
            return (String)session.getAttribute("defaultPage"); // 스트라바 연동버튼을 눌러 결국 토큰을 받는데 실패했을 때는 그냥 원래 페이지로 돌아간다.
        }





    }


}