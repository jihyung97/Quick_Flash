package com.quickflash.mileage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name="mileage")
@Entity
public class MileageEntity {
    @Id
    private Integer userId;
    private Double mileageOfCycle;
    private Double mileageOfRunning;

    @CreationTimestamp
    private LocalDate createdAt; //
}
