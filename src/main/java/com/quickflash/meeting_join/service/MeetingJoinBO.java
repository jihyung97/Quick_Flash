package com.quickflash.meeting_join.service;


import com.quickflash.meeting_join.dto.MeetingJoinDto;
import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.meeting_join.repository.MeetingJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

   public List<MeetingJoinDto> getMeetingJoinListForDtoByPostId(int postId){
        return     meetingJoinMapper.selectMeetingJoinListForDtoByPostId(postId);
   }

}
