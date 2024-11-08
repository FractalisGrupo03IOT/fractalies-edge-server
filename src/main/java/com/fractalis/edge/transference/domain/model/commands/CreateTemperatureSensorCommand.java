package com.fractalis.edge.transference.domain.model.commands;

public record CreateTemperatureSensorCommand(String iotDeviceId, Long temperature) {
}
