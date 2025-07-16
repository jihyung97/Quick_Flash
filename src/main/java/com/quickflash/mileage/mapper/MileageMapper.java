package com.quickflash.mileage.mapper;

import com.quickflash.mileage.domain.Mileage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface MileageMapper {
    Mileage updateMileage(Mileage mileage);
    Mileage insertMileage(Mileage mileage);
    Mileage selectMileageByUserIdAndCreatedAt(@Param("userId") int userId, @Param("createdAt") LocalDate createdAt);
}