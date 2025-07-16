package com.quickflash.mileage.mapper;

import com.quickflash.mileage.domain.Mileage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MileageMapper {
    Mileage updateMileage(Mileage mileage);
    Mileage insertMileage(Mileage mileage);
    Mileage selectMileageByUserIdAndCreatedAt(@Param("userId") int userId, @Param("createdAt") LocalDate createdAt);

    List<Mileage> selectMileageForBatch(@Param("startId") int startId, @Param("batchSize") int batchSize );
     Integer selectMileageIdForDateStandard(LocalDate date);

}