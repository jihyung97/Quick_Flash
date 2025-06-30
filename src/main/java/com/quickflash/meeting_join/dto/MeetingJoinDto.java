package com.quickflash.meeting_join.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MeetingJoinDto {
    private int userId;
    private int postId;
    private String userName;
    //Double로 안하면 mapper에서 오류발생(speed,power안가져올 때 createdAt을 가져오려 함)
     private double speed; // 러닝일때
     private double power; // 사이클
    private LocalDateTime createdAt;
}
