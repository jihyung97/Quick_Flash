package com.quickflash.user.mapper;

import com.quickflash.user.dto.UserIdToNameDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {


   List<UserIdToNameDto> selectIdToUserNameMapByIdSet(@Param("idSet") Set<Integer> idSet);
   List<Integer> selectUserIdsForBatch(@Param("offset") int offset, @Param("limit") int limit);

}
