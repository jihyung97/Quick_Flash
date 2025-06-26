package com.quickflash.comment.service;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.entity.CommentEntity;
import com.quickflash.comment.mapper.CommentMapper;
import com.quickflash.comment.repository.CommentRepository;
import com.quickflash.join.entity.JoinEntity;
import com.quickflash.join.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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



}