package com.quickflash.api.strava.OAuthClient;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StravaTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_at")
    private long expiresAt;

    private Athlete athlete;

    @Getter
    @Setter
    public static class Athlete {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
    }
}