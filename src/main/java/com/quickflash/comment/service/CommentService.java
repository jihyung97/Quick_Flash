package com.quickflash.comment.service;

import com.quickflash.comment.dto.CommentDto;
import com.quickflash.comment.mapper.CommentMapper;
import com.quickflash.meetingPost.service.MeetingPostBO;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.user.dto.UserIdToNameDto;
import com.quickflash.user.service.UserBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final UserBO userBO;
    private final CommentMapper commentMapper;
    private final MeetingPostBO meetingPostBO;


    public List<CommentDto> generateCommentDtoListByPostId(int postId){
        List<CommentDto>  commentDtoList = commentMapper.selectCommentDtoListByPostId(postId);
        Set<Integer> userIdSet = new HashSet<>();

        for(CommentDto commentDto : commentDtoList){
            userIdSet.add(commentDto.getUserId());
        }
        // 쿼리 실행을 n번에서 1번으로 줄여서  db의 부담을 최소화 한다
        List<UserIdToNameDto> userIdToNameList =  userBO.getIdToUserNameMapByIdSet(userIdSet);
        Map<Integer,String> userIdToNameMap = new HashMap<>();
        if(userIdToNameList!= null){
            for(UserIdToNameDto userIdToName : userIdToNameList){
                userIdToNameMap.put(userIdToName.getId(),userIdToName.getName());


            }
        }

        log.info(" idToUserNameMap  {}",userIdToNameMap);
        for(CommentDto commentDto : commentDtoList){
            commentDto.setUserName(userIdToNameMap.get(commentDto.getUserId()));
            log.info("commentDto.getUserId()  {}",commentDto.getUserId());
            log.info("id에서 UserName으로바꾼 값  {}",userIdToNameMap.get( commentDto.getUserId()));
        }
        log.info("commentDtoList 는 {}",commentDtoList);
        return commentDtoList;
    }
    //commentDtoList 는 이미 최신순으로 나열되어 있으므로 그냥 순회하며 before after 구분해 넣어도 각각 최신순 됨
    public Map<String,List<CommentDto>> seperateCommentDtoListByBeforeOrAfter(List<CommentDto> commentDtoList){
        Map<String,List<CommentDto>> result = new HashMap<>();
        List<CommentDto> commentDtoListBefore = new ArrayList<>();
        List<CommentDto> commentDtoListAfter = new ArrayList<>();
        for(CommentDto commentDto : commentDtoList){
            if(commentDto.isBeforeMeeting()){
                commentDtoListBefore.add(commentDto);
            }else{
                commentDtoListAfter.add(commentDto);
            }
        }
        result.put("before", commentDtoListBefore);
        result.put("after", commentDtoListAfter);
        return result;
    }

    public Qualification isCommentCreateOk(int postId, int sessionId){
        if(!meetingPostBO.isPostExist(postId)){
            return Qualification.POST_NOT_EXIST;
        }
        return Qualification.OK;

    }

    public Qualification isCommentUpdateOk(int postId, int sessionId){
        if(!meetingPostBO.isPostExist(postId)){
            return Qualification.POST_NOT_EXIST;
        }
        return Qualification.OK;

    }

    public Qualification isCommentDeleteOk(int postId, int sessionId){
        if(!meetingPostBO.isPostExist(postId)){
            return Qualification.POST_NOT_EXIST;
        }
        return Qualification.OK;

    }



}