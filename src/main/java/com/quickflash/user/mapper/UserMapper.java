package com.quickflash.user.mapper;

import com.quickflash.meetingPost.domain.MeetingPost;
import com.quickflash.user.dto.userIdToNameDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserMapper {


   List<userIdToNameDto> selectIdToUserNameMapByIdSet(@Param("idSet") Set<Integer> idSet);
   List<Integer> selectUserIdsForBatch(@Param("offset") int offset, @Param("limit") int limit);

}
