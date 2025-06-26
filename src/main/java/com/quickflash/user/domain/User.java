package com.quickflash.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class User {

    private int id;
    private String loginId;
    private String password;
    private String name;
    private String defaultLocation;
    private String defaultLocationPoint;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}

