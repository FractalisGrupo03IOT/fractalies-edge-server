package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.commands.CreateCropLinkCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateHumiditySensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateSunlightSensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateTemperatureSensorCommand;

public interface CropLinkCommandService {
    Long handle(CreateCropLinkCommand command);
    void handle(CreateTemperatureSensorCommand command);
    void handle(CreateHumiditySensorCommand command);
    void handle(CreateSunlightSensorCommand command);
}
