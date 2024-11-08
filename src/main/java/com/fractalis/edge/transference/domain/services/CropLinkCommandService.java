package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.commands.*;

public interface CropLinkCommandService {
    Long handle(CreateCropLinkCommand command);
    void handle(CreateTemperatureSensorCommand command);
    void handle(CreateHumiditySensorCommand command);
    void handle(CreateSunlightSensorCommand command);
    void handle(InitializeIotDeviceCommand command);
    void handle(UnlinkIotDeviceCommand command);

    void sendToExternalService();
}
