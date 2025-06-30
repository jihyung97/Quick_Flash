package com.quickflash.comment.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
public class CommentDto {
    private int id; //나중에 지울때 반드시 필요함
    private String userName;
    private int userId;
    private int postId;
    private String content;
    private boolean isBeforeMeeting;

}