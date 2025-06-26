package com.quickflash.comment.service;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.entity.CommentEntity;
import com.quickflash.comment.mapper.CommentMapper;
import com.quickflash.comment.repository.CommentRepository;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final UserBO userBO;
    private final CommentMapper commentMapper;


    public List<CommentDto> generateCommentDtoListByPostId(int postId){
        List<CommentDto>  commentDtoList = commentMapper.selectCommentDtoListByPostId(postId);
        Set<Integer> userIdSet = new HashSet<>();
        Map<Integer, String> idToUserName;
        for(CommentDto commentDto : commentDtoList){
            userIdSet.add(commentDto.getUserId());
        }
        // 쿼리 실행을 n번에서 1번으로 줄여서  db의 부담을 최소화 한다
        idToUserName = userBO.getIdToUserNameMapByIdSet(userIdSet);
        for(CommentDto commentDto : commentDtoList){
            commentDto.setUserName(idToUserName.get(commentDto.getUserId()));
        }
        return commentDtoList;
    }



}