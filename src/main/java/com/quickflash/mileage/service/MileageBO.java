package com.quickflash.mileage.service;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.mapper.MeetingPostMapper;
import com.quickflash.meetingPost.repository.MeetingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MileageBO {
    private final MeetingPostMapper meetingPostMapper;
    private final MeetingPostRepository meetingPostRepository;


    public boolean isPostExist(int postId){
        return meetingPostRepository.existsById(postId);
    }
    public boolean isUserLeader(int sessionId, int postId){
        return sessionId == meetingPostMapper.getUserIdById(postId);
    }

    public int addMeetingPost(
            MeetingPost meetingPost
    ){
        return meetingPostMapper.insertMeetingPost(meetingPost);
    }

    public int updateStatusById (String currentStatus, int id){

        return meetingPo