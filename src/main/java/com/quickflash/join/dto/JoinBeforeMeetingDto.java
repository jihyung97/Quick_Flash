package com.quickflash.join.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinBeforeMeetingDto {
    private int userId;
    private int postId;
    private String userName;
}
