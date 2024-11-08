package com.fractalis.edge.transference.domain.model.commands;

public record InitializeIotDeviceCommand(Long cropId, String iotDeviceId) {
}
