package com.quickflash.api.strava.StravaApi;

import com.quickflash.api.strava.OAuthClient.StravaOAuthClient;
import com.quickflash.api.strava.OAuthClient.StravaTokenEntity;
import com.quickflash.api.strava.OAuthClient.StravaTokenService;
import com.quickflash.api.strava.StravaApi.entity.DataForAbilityOfCycle;
import com.quickflash.meetingPost.service.Response;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/strava-api")
@Slf4j
public class StravaApiController {

    private final StravaTokenService stravaTokenService;
    private final StravaApi stravaApi;
    private final StravaOAuthClient stravaOAuthClient;
    private final StravaApiService stravaApiService;
    @RequestMapping("/connect-cycle")
    public Map<String, Object> connectCycle(HttpSession session,
                                            @RequestParam(value = "pageUrl", defaultValue = "") String pageUrl){

        Map<String,Object> result = new HashMap<>();
        Integer sessionId = (Integer) session.getAttribute("userId");
        if(pageUrl != null && pageUrl.isEmpty()){
            session.setAttribute("defaultPage", pageUrl); // session에 버튼을 누른 페이지의 url 저장
        }

          session.setAttribute("connect-exercise","/strava-api/connect-cycle");
        // 게시글 작성하기 전에 로그인 됬는지, 이미 작성된 글인지 확인.
        if(sessionId == null) {
            result.put("result","fail");
            return result;
        }
        StravaTokenEntity token = stravaTokenService.getAccessToken(sessionId).orElse(null);
        //token이 없거나 유효하지 않으면  redirect: buildAuthorizeUrl()
        if(token == null || token.getExpiresAt().isBefore(LocalDateTime.now())){
            result.put("url",   stravaOAuthClient.buildAuthorizeUrl( ));
            result.put("result","fail_get_token");
          return result;
        }
        // redirect해서 토큰을 받고 db에 저장하는 과정까지 마치면 db에서 토큰가져옴

        try {
            List<DataForAbilityOfCycle> abilityOfCycleList = stravaApi.getActivitiesLastMonth(token.getAccessToken());
            stravaApiService.calculateAndSaveCycle(abilityOfCycleList,sessionId);
            result.put("result","success");
            return result;
        }catch(Exception e){
            e.printStackTrace();
            result.put("result","fail_to_calculate_and_save"); // 추후에 fail을 세분화해서 각각 어디 url로 향할지 정할것
            return result;
        }
    }
}
