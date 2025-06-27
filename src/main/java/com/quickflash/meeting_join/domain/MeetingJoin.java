package com.quickflash.meeting_join.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingJoin {

    private int id;
    private int userId;
    private int postId;
    private String userName;
    private String joinStatus;
    private String isSafetyAgree;

    private String createdAt;


    private String updatedAt;

}