package com.quickflash.meetingPost.mapper;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface MeetingPostMapper {
    int getUserIdById(int id);
    int insertMeetingPost(MeetingPost meetingPost);
    int updateStatusById(@Param("currentStatus") String currentStatus, @Param("id") int id);
    Map<String, Object> selectExpiredAtAndStatusById(int id);
    MeetingPost selectMeetingPostById(int id);
    List<Map<String,Object>> selectMeetingPostListForThumbnailTest();

    int updateMeetingPost(MeetingPost meetingPost);
    int updateMeetingPostBeforeMeetingById (MeetingPost meetingPost);
    int updateMeetingPostAfterMeeting(MeetingPost meetingPost);
    int selectMaxCountById(int id);



}
