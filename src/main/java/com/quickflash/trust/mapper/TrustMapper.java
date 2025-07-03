package com.quickflash.trust.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TrustMapper {
   public int updateTrustOfMemberByBatch(@Param("trustOfMemberMap") Map<Integer,Double> trustOfMemberMap);

}
