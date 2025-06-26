package com.quickflash.join.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
public class Join{

    private int id;
    private int userId;
    private int postId;
    private String userName;
    private String joinStatus;
    private String isSafetyAgree;

    private String createdAt;


    private String updatedAt;

}