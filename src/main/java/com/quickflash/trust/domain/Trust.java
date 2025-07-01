package com.quickflash.trust.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Trust {
    private int userId;
    private Double trustOfLeader;
    private Double trustOfMember;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
