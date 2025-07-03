package com.quickflash.api.strava.OAuthClient;

import com.quickflash.api.strava.repository.StravaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.ZoneId.*;

@Service
@RequiredArgsConstructor
public class StravaTokenService {

    private final StravaTokenRepository stravaTokenRepository;

    public void addStravaToken(Integer userId, StravaTokenResponse stravaTokenResponse) {
        StravaTokenEntity token = stravaTokenRepository.findById(userId)
                .orElseGet( () -> new StravaTokenEntity());

        token.setUserId(userId);
        token.setAccessToken(stravaTokenResponse.getAccessToken());
        token.setRefreshToken(stravaTokenResponse.getRefreshToken());
        token.setTokenType(stravaTokenResponse.getTokenType());
        token.setExpiresIn(stravaTokenResponse.getExpiresIn());
        token.setExpiresAt(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(stravaTokenResponse.getExpiresAt()),
                ZoneId.systemDefault()));

        stravaTokenRepository.save(token);
    }





}