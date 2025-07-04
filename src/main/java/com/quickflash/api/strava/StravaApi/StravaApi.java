package com.quickflash.api.strava.StravaApi;

import com.quickflash.api.strava.StravaApi.entity.DataForAbilityOfCycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class StravaApi {

    private final WebClient webClient;


    public List<DataForAbilityOfCycle> getActivitiesLastMonth(String accessToken) {
        long oneMonthAgoEpoch = Instant.now().minus(1000, ChronoUnit.DAYS).getEpochSecond();

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host("www.strava.com")
                    .path("/api/v3/athlete/activities")
                    .queryParam("after", oneMonthAgoEpoch)
                    .queryParam("per_page", 100)
                    .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()  // 서버에 보냄
            .bodyToFlux(DataForAbilityOfCycle.class) // flux형태로 데이터를 가져옴
            .collectList() // flux형태를 list 로 바꿈
            .block(); //비동기 -> 동기
    }
}
