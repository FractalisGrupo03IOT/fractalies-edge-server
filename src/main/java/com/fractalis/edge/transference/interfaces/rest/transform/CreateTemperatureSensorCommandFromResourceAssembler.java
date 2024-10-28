package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.commands.CreateTemperatureSensorCommand;
import com.fractalis.edge.transference.interfaces.rest.resources.CreateTemperatureSensorResource;

public class CreateTemperatureSensorCommandFromResourceAssembler {
    public static CreateTemperatureSensorCommand toCommandFromResource(CreateTemperatureSensorResource resource) {
        return new CreateTemperatureSensorCommand(resource.cropCloudId(), resource.temperature());
    }
}
