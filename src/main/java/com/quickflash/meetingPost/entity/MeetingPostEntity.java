package com.quickflash.meetingPost.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name="meetingPost")
@Entity
public class MeetingPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;

    private String title;
    private String location;


    // 위경도 좌표: POINT에서 분리
    private Double latitude;   // 위도 (Y)
    private Double longitude;  // 경도 (X)

    private String restLocation;
    private LocalDateTime expiredAt;
    private String contentText;
    private String afterMeetingContent;

    private String exerciseType;
    private Double distance;
    private Double speed;
    private Double power;

    private Integer minHeadCount;
    private Integer maxHeadCount;
    private Boolean isRestExist;
    private Boolean isAbandonOkay;
    private Boolean isAfterPartyExist;
    private Boolean isLocationConnectedToKakao;
    private Boolean isUserAbilityConnectedToStrava;
    private Boolean isMyPaceShown;
    private Boolean isMyFtpShown;

    private String currentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
