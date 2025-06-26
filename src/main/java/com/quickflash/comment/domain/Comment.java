package com.quickflash.comment.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Data
@Builder
public class Comment{

    private int id;
    private int postId;
    private int userId;
    private String content;
    private boolean isBeforeMeeting;
    private String createdAt;
    private String updatedAt;


}