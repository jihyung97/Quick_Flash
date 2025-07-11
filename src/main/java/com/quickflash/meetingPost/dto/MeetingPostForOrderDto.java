package com.quickflash.meetingPost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor //
public class MeetingPostForOrderDto {
    private int id;
    private Integer userId;
    private Double latitude;   // 위도 (Y)
    private Double longitude;  // 경도 (X)
    private String exerciseType;
    private Double speed;
    private Double power;
}
