package me.dcs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "ChargeDetailRecord")
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ChargeDetailRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionIdentification;

    @Column(nullable = false)
    private String vehicleIdentification;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Double cost;

    public ChargeDetailRecordDTO convertToDTO() {
        return ChargeDetailRecordDTO.builder().id(sessionIdentification).vehicleIdentification(vehicleIdentification).startTime(startTime).endTime(endTime).cost(cost).build();
    }

}
