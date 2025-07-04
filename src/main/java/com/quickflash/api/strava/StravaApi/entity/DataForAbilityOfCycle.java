package com.quickflash.api.strava.StravaApi.entity;

import lombok.Data;

@Data
public class DataForAbilityOfCycle {
    private Long id;

    private Integer average_watts; // 평균 파워(W), null일 수 있음

}