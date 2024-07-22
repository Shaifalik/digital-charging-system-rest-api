package me.dcs.utils;

public class ApplicationConstants {
    public final static String INVALID_SESSION_ID = "Invalid Session Id, not found";
    public final static String INVALID_VEHICLE_ID = "Invalid Vehicle Id, not found";
    public final static String INCORRECT_TIME = "End time cannot be smaller than Start time";
    public final static String INCORRECT_OBJECT = "Validation is only applied to ChargeDetailRecord";
    public final static String VEHICLE_IDENTIFICATION_MUST_NOT_BE_NULL = "VehicleIdentification must not be null";
    public final static String COST_MUST_NOT_BE_NULL = "Cost must not be null";
    public final static String COST_MUST_BE_POSITIVE = "Cost must be positive";
    public final static String START_TIME_MUST_NOT_BE_NULL = "StartTime must not be null";
    public final static String END_TIME_MUST_NOT_BE_NULL = "EndTime must not be null";
    public final static String OUTDATED_RECORD = "Charge Detail Record is outdated, " +
            "start time should be after the end time of last record";
}
