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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class StravaOAuthClient {

    private final StravaOAuthProperties properties;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final StravaOAuthProperties stravaOAuthProperties;



    public String buildAuthorizeUrl( ) {
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
        return webClient.post()
                . uri("https://www.strava.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("client_id", properties.getClientId())
                        .with("client_secret", properties.getClientSecret())
                        .with("code", code)
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(StravaTokenResponse.class)
                .block();
    }
}

