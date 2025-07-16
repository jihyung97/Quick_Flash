package com.quickflash.mileage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mileage {

    private int userId;
  private Double mileageOfCycle;
  private Double mileageOfRunning;
  private LocalDate createdAt; //

}
