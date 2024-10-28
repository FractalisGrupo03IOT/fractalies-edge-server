package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.commands.CreateSunlightSensorCommand;
import com.fractalis.edge.transference.interfaces.rest.resources.CreateSunlightSensorResource;

public class CreateSunlightSensorCommandFromResourceAssembler {
    public static CreateSunlightSensorCommand toCommandFromResource(CreateSunlightSensorResource resource) {
        return new CreateSunlightSensorCommand(resource.cropCloudId(), resource.sunlight());
    }
}
