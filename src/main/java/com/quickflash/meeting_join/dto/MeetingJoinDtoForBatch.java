package com.quickflash.meeting_join.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingJoinDtoForBatch {
    private int id;
    private int userId;
    private String joinStatus;

}
