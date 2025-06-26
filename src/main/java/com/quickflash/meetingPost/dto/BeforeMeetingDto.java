package com.quickflash.meetingPost.dto;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.entity.CommentEntity;
import com.quickflash.join.dto.JoinBeforeMeetingDto;
import lombok.Builder;
import lombok.Data;
import org.hibernate.mapping.Join;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class BeforeMeetingDto {
    private Integer postId;
    private Integer userId;

    private String title;
    private String location;


    // 위경도 좌표: POINT에서 분리
    private Double latitude;   // 위도 (Y)
    private Double longitude;  // 경도 (X)
    private String restLocation;

    private LocalDateTime expiredAt;
    private String contentText;


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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<JoinBeforeMeetingDto> joinList;
    List<CommentDto> commentList;
    private String userName;

}
