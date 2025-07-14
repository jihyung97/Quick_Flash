package com.quickflash.meetingPost.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties({"@class"})

@NoArgsConstructor
@AllArgsConstructor
public class MeetingPostElasticIndexDto {

        private Integer id;
        private String title;
        private String contentText;
        private String afterMeetingContent;
        private String exerciseType;

        private String location; // 사람이 읽는 주소
        private Double latitude;
        private Double longitude;

        private Double distance;
        private Double speed;
        private Double power;

        private Integer maxHeadCount;
        private Integer duration;
        private Integer height;

        private String currentStatus;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime expiredAt;

}
