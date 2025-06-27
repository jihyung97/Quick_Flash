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
    private double speed; // 러닝일때
    private double power; // 사이클
    private LocalDateTime joinedAt;
}
