package com.quickflash.meetingPost.mapper;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.meetingPost.dto.MeetingPostForOrderDto;
import com.quickflash.meetingPost.dto.ThumbnailDto;
import org.apache.ibatis.annotations.MapKey;
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

    //bound-box 로 게시글들을 추려서 계산에 필요한 스키마들을 dto로 가져온다
//    @MapKey("id")
//   Map<Integer,MeetingPostForOrderDto> selectMeetingPostForOrderDtoMapByBoundBox(Map<String,Double> minMaxLatLng) ;

    List<ThumbnailDto> selectThumbnailDtoListByPostIds(List<Integer> postIds);
    List<Map<String,Object>>  selectMeetingPostMapForOneClickByBoundBox(Map<String,Object> minMaxLatLngAndDistance);
    Integer selectPostIdForDateStandard(LocalDateTime date);
    @MapKey("id")
   Map<Integer,MeetingPostForOrderDto>   selectMeetingPostForOrderDtoMapByBoundBoxAndIdForDate(Map<String,Object> minMaxLatLngAndId);
    Double selectDistanceById(int id);



}
