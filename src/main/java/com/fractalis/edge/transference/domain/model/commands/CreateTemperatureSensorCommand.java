package com.fractalis.edge.transference.domain.model.commands;

public record CreateTemperatureSensorCommand(Long cropCloudId, Long temperature) {
}
