package com.quickflash.meetingPost.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class OneClickDto {
    int  id;
    int start_time; //  시간단위 hour
    int end_time;
    int height; // 단위 : 10m당 1, ex) 1000m -> 100
    double sst;
}
