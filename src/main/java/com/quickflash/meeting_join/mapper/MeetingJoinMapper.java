package com.quickflash.meeting_join.mapper;

import com.quickflash.meeting_join.dto.MeetingJoinDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetingJoinMapper {
    List<MeetingJoinDto> selectMeetingJoinListForDtoByPostId(int postId);



}