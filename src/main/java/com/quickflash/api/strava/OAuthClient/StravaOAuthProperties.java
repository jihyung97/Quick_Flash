package com.quickflash.api.strava.OAuthClient;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "strava")
@Builder
@Getter
@Slf4j
public class StravaOAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
