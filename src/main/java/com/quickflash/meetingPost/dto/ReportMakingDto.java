package com.quickflash.meetingPost.dto;

import com.quickflash.meeting_join.dto.MeetingJoinDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReportMakingDto {
    private int postId;
    private int userId;
    private String userName;
    private String title;
    private String location;
    private String contentText;
    private String exerciseType;
    private Double distance;
    private Double speed;
    private Double power;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    List<MeetingJoinDto> meetingJoinList;
}
