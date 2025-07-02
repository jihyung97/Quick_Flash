package com.quickflash.trust.mapper;

import com.quickflash.user.dto.userIdToNameDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface TrustMapper {
   public int updateTrustOfMemberByBatch(@Param("trustOfMemberMap") Map<Integer,Double> trustOfMemberMap);

}
