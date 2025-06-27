package com.quickflash.user.mapper;

import com.quickflash.meetingPost.domain.MeetingPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserMapper {
   Map<Integer, String> selectIdToUserNameMapByIdSet(@Param("idSet") Set<Integer> idSet);

}
