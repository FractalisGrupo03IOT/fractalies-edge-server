package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.commands.CreateHumiditySensorCommand;
import com.fractalis.edge.transference.interfaces.rest.resources.CreateHumiditySensorResource;

public class CreateHumiditySensorCommandFromResourceAssembler {
    public static CreateHumiditySensorCommand toCommandFromResource(CreateHumiditySensorResource resource) {
        return new CreateHumiditySensorCommand(resource.iotDeviceId(), resource.humidity());
    }
}
