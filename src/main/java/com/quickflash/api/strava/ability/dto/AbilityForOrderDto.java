package com.quickflash.api.strava.ability.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AbilityForOrderDto {

    private int userId;
    private double maxCyclingAvgPower;
    private double maxRunningSpeed;
}
