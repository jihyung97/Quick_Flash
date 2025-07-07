package com.quickflash.meetingPost.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class ThumbnailDto {
    int id;
    String title;
    String location;
    LocalDateTime expiredAt;
    String exerciseType;


    double distance;
    double speed;

    double power;
    int minHeadCount;
    int maxHeadCount;

    boolean isRestExist;
    boolean isAbandonOkay;
    LocalDateTime createdAt;
   Double leaderPace;
    Double leaderFtp;

    String remainedTime;
    String leaderName;
    Integer currentHeadCount;
    Integer speed_min;
    Integer speed_sec;

}