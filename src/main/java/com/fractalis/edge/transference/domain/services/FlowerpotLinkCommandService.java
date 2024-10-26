package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.commands.CreateFlowerpotLinkCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateHumiditySensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateSunlightSensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateTemperatureSensorCommand;

public interface FlowerpotLinkCommandService {
    Long handle(CreateFlowerpotLinkCommand command);
    void handle(CreateTemperatureSensorCommand command);
    void handle(CreateHumiditySensorCommand command);
    void handle(CreateSunlightSensorCommand command);
}
