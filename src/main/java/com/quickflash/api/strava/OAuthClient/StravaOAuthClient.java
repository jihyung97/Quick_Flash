package com.quickflash.api.strava.OAuthClient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class StravaOAuthClient {

    private final StravaOAuthProperties properties;
    private final RestTemplate restTemplate;



    public String buildAuthorizeUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("www.strava.com")
                .path("/oauth/authorize")
                .queryParam("client_id", properties.getClientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("scope", "read,activity:read")
                .queryParam("approval_prompt", "auto")
                .build()
                .toUriString();
    }

    
    //authentication code를 parameter로 받고 code와 client id, secret,code, 등을 넣어 서버에 보내고 토큰을 받음
    public StravaTokenResponse exchangeCodeForToken(String code) {
        String url = "https://www.strava.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", properties.getClientId() );
        params.add("client_secret", properties.getClientSecret());
        params.add("code", code);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<StravaTokenResponse> response =
                restTemplate.postForEntity(url, request, StravaTokenResponse.class);

        return response.getBody();
    }
}

