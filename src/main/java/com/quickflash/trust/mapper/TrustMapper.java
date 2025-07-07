package com.quickflash.trust.mapper;

import com.quickflash.trust.dto.TrustForOrderDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TrustMapper {
   public int updateTrustOfMemberByBatch(@Param("trustOfMemberMap") Map<Integer,Double> trustOfMemberMap);
   @MapKey("userId")
   public Map<Integer, TrustForOrderDto> selectTrustForOrderDtoMapByUserIdList(@Param("userIdList") List<Integer> userIdList);

}
