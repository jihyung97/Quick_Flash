package com.quickflash.meeting_join.service;


import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.service.Qualification;
import com.quickflash.meetingPost.service.Response;
import com.quickflash.meetingPost.service.Status;
import com.quickflash.meeting_join.dto.MeetingJoinDto;
import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.meeting_join.repository.MeetingJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MeetingJoinBO {
    private final MeetingJoinRepository meetingJoinRepository;
    private final MeetingJoinMapper meetingJoinMapper;
    List<MeetingJoinEntity> getMeetingJoinEntityListByPostId(int postId){
       return meetingJoinRepository.findByPostId(postId);
    }

    public Boolean isIMember(int postId, int userId){
        return meetingJoinRepository.findByPostIdAndUserId(postId,userId).isPresent();
    }

   public List<Map<String,Object>> getMeetingJoinListForDtoByPostId(int postId){
        return     meetingJoinMapper.selectMeetingJoinListForDtoByPostId(postId);
   }

    public boolean updateMeetingJoinStatusByPostIdAndUserIdList (int postId,Map<Integer,String> userIdToJoinStatus){
        return     meetingJoinMapper.updateMeetingJoinList(postId, userIdToJoinStatus);
    }

    @Transactional
    public Response deleteMeetingJoin(
            int userId,
            int postId

    ){
        try{
            Optional<MeetingJoinEntity> meetingJoinEntity = meetingJoinRepository.findByPostIdAndUserId(postId,userId);
            if(meetingJoinEntity.isPresent()){
                meetingJoinRepository.delete(meetingJoinEntity.get());
            }else{
                return Response.POST_NOT_EXIST;
            }

           return Response.RESULT_OK;
        } catch (Exception e) {
           return Response.FAILED;
        }


    }
}

