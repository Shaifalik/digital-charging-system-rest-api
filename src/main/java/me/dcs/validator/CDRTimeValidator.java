package me.dcs.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.dcs.model.ChargeDetailRecordDTO;

public class CDRTimeValidator implements ConstraintValidator<CDRTimeValidation, ChargeDetailRecordDTO> {

    public boolean isValid(ChargeDetailRecordDTO dto, ConstraintValidatorContext context) {
        return dto.getEndTime().isAfter(dto.getStartTime());
    }
}
