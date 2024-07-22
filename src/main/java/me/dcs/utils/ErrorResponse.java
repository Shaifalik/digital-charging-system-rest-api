package me.dcs.utils;

public record ErrorResponse(int status, String message, long timestamp) {
}
