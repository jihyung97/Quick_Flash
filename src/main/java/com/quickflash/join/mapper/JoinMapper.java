package com.quickflash.join.mapper;

import com.quickflash.join.dto.JoinBeforeMeetingDto;
import com.quickflash.meetingPost.domain.MeetingPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JoinMapper {
    List<JoinBeforeMeetingDto> selectJoinBeforeMeetingDtoListByPostId(int postId);


}