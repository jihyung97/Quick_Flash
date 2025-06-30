package com.quickflash.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class userIdToNameDto {
    int userId;
   String name;
}
