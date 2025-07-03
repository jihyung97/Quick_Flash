package com.quickflash.api.strava.OAuthClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="strava_token")
@Entity
public class StravaTokenEntity {


    @Id
    private Integer userId;


    private String accessToken;


    private String refreshToken;


    private String tokenType;

    private Long expiresIn;

    private LocalDateTime expiresAt;


}