package com.quickflash.meetingPost.dto;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailDto {
    @Id
    int id;
    String title;
    String location;
    LocalDateTime expiredAt;
    String exerciseType;


    Double distance;
    Double speed;

    Double power;
    Integer minHeadCount;
    Integer maxHeadCount;

    Boolean isRestExist;
    Boolean isAbandonOkay;
    LocalDateTime createdAt;
   Double leaderPace;
    Double leaderFtp;

    String remainedTime;
    String leaderName;
    Integer currentHeadCount;
    Integer speed_min;
    Integer speed_sec;
    Double score;

}