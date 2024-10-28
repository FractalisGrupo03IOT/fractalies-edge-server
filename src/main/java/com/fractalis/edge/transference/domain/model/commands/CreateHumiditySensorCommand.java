package com.fractalis.edge.transference.domain.model.commands;

public record CreateHumiditySensorCommand(Long cropCloudId, Long humidity) {
}
