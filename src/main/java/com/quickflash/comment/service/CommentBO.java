package com.quickflash.comment.service;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.entity.CommentEntity;
import com.quickflash.comment.mapper.CommentMapper;
import com.quickflash.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentBO {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentEntity> getCommentListByPostId(int postId){
        return commentRepository.findByPostId(postId);
    }

    public List<CommentDto> getCommentDtoListByPostId(int postId){
        return commentMapper.selectCommentDtoListByPostId(postId);
    }
    @Transactional
    public boolean addComment(int userId,int postId, String content, boolean isBeforeMeeting){
        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userId)
                .postId(postId)
                .content(content)
                .isBeforeMeeting(isBeforeMeeting)
                .build();
        return commentRepository.save(commentEntity) != null ? true : false;
    }
    @Transactional
    public boolean deleteComment(int id) {
        Optional<CommentEntity> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            commentRepository.delete(optional.get());
            return true;
        }
        return false;
    }




}