package com.fractalis.edge.transference.domain.model.commands;

public record CreateHumiditySensorCommand(String iotDeviceId, Long humidity) {
}
