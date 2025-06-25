package com.quickflash.join.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name="join")
@Entity
public class JoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int postId;
    private String userName;
    private String joinStatus;
    private String isSafetyAgree;
    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;



}