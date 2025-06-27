package com.quickflash.meeting_join.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name="meeting_join")
@Entity
public class MeetingJoinEntity {
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