package com.quickflash.trust.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrustForOrderDto {

    private int userId;
    private Double trustOfLeader;
    private Double trustOfMember;

}
