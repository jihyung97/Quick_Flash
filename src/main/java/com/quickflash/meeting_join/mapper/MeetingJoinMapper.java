package com.quickflash.meeting_join.mapper;

import com.quickflash.meeting_join.domain.MeetingJoin;
import com.quickflash.meeting_join.dto.MeetingJoinDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MeetingJoinMapper {
    List<Map<String,Object>> selectMeetingJoinListForDtoByPostId(int postId);

    boolean updateMeetingJoinList(@Param("postId") int postId,
                                  @Param("userIdToJoinStatus") Map<Integer, String> userIdToJoinStatus);
    List<MeetingJoin> selectMeetingJoinForTrustBatch(@Param("userIdList")List<Integer> userIdList , @Param("oneMonthAgo")   LocalDateTime oneMonthAgo) ;;


}