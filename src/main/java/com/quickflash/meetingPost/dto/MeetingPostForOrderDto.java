package com.quickflash.meetingPost.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingPostForOrderDto {
    private int id;
    private Integer userId;
    private Double latitude;   // 위도 (Y)
    private Double longitude;  // 경도 (X)
    private String exerciseType;
    private Double speed;
    private Double power;
}
