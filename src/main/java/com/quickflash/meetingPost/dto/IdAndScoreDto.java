package com.quickflash.meetingPost.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder


@NoArgsConstructor
@AllArgsConstructor
public class IdAndScoreDto {
    private int id;
    private Double score;
    private LocalDateTime expiredAt;
}
