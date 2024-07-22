package me.dcs.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import me.dcs.validator.CDRTimeValidation;
import me.dcs.validator.CustomValidationGroup;
import me.dcs.validator.NotNullGroup;

import java.time.LocalDateTime;

import static me.dcs.utils.ApplicationConstants.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@CDRTimeValidation(groups = CustomValidationGroup.class)
public class ChargeDetailRecordDTO {

    @Schema(description = "Unique identifier for the record, auto-generated", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotNull
    @Schema(description = "Vehicle identification string", example = "ABC123",
            minLength = 1, maxLength = 100)
    @NotEmpty(message = VEHICLE_IDENTIFICATION_MUST_NOT_BE_NULL)
    private String vehicleIdentification;

    @Schema(description = "Start time of the record", example = "2024-07-27T14:20:50.110Z",
            format = "date-time")
    @NotNull(groups = NotNullGroup.class, message = START_TIME_MUST_NOT_BE_NULL)
    private LocalDateTime startTime;

    @Schema(description = "End time of the record", example = "2024-07-27T14:30:50.110Z",
            format = "date-time")
    @NotNull(groups = NotNullGroup.class, message = END_TIME_MUST_NOT_BE_NULL)
    private LocalDateTime endTime;

    @Schema(description = "Cost of the record, must be positive", example = "10.0")
    @NotNull(message = COST_MUST_NOT_BE_NULL)
    @Positive(message = COST_MUST_BE_POSITIVE)
    private Double cost;

    public ChargeDetailRecord convertToDO() {
        return ChargeDetailRecord.builder()
                .vehicleIdentification(vehicleIdentification)
                .startTime(startTime)
                .endTime(endTime)
                .cost(cost)
                .build();
    }

}
