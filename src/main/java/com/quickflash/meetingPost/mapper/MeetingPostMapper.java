package com.quickflash.meetingPost.mapper;

import com.quickflash.meetingPost.domain.MeetingPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface MeetingPostMapper {
    int getUserIdById(int postId);
    int insertMeetingPost(MeetingPost meetingPost);
    int updateStatusById(@Param("currentStatus") String currentStatus, @Param("postId") int id);
    Map<String, Object> selectExpiredAtAndStatusById(int id);


}
