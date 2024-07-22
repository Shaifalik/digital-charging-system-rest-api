package me.dcs.utils;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;
import me.dcs.validator.CustomValidationGroup;
import me.dcs.validator.NotNullGroup;

@GroupSequence({Default.class, NotNullGroup.class, CustomValidationGroup.class})
public interface ValidationSequence {
}