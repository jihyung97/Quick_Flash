package com.quickflash.meeting_join.mapper;

import com.quickflash.meeting_join.dto.MeetingJoinDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeetingJoinMapper {
    List<Map<String,Object>> selectMeetingJoinListForDtoByPostId(int postId);

    boolean updateMeetingJoinList(@Param("postId") int postId,
                                  @Param("userIdToJoinStatus") Map<Integer, String> userIdToJoinStatus);


}