package com.quickflash.user.dto;

import lombok.Builder;
import lombok.Data;

 @Data
@Builder
public class UserIdToNameDto {
    int id;
    String name;
}
