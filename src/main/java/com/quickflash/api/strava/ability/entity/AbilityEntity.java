package com.quickflash.api.strava.ability.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name="ability")
@Entity
public class AbilityEntity {
    @Id

    private int userId;
    private double maxCyclingAvgPower;
    private double maxRunningSpeed;
    private boolean isCycleConnectedToStrava;
        private boolean isRunConnectedToStrava;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
