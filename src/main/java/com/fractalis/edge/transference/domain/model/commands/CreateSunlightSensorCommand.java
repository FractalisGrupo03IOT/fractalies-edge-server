package com.fractalis.edge.transference.domain.model.commands;

public record CreateSunlightSensorCommand(String iotDeviceId, Long sunlight) {
}
