package com.quickflash.meetingPost.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class ThumbnailDto {
    int postId;
    String title;
    String location;
    LocalDateTime expiredAt;
   String remainedTime;
    String exerciseType;
    double distance;
    double speed;
   Integer speed_min;
    Integer speed_sec;
    double power;
    int minHeadCount;
    int maxHeadCount;
    boolean isAbandonOkay;
    boolean isRestExist;
    double leaderPace;
    double leaderFtp;
    LocalDateTime createdAt;
    String leaderName;
    int currentHeadCount;

}
