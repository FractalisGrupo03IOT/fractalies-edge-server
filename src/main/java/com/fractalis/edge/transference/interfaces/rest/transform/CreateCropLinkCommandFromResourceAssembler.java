package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.commands.CreateCropLinkCommand;
import com.fractalis.edge.transference.interfaces.rest.resources.CreateCropLinkResource;

public class CreateCropLinkCommandFromResourceAssembler {
    public static CreateCropLinkCommand toCommandFromResource(CreateCropLinkResource resource) {
        return new CreateCropLinkCommand(resource.cropCloudId());
    }
}
