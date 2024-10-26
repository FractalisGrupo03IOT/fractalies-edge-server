package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.commands.CreateFlowerpotLinkCommand;
import com.fractalis.edge.transference.interfaces.rest.resources.CreateFlowerpotLinkResource;

public class CreateFlowerpotLinkCommandFromResourceAssembler {
    public static CreateFlowerpotLinkCommand toCommandFromResource(CreateFlowerpotLinkResource resource) {
        return new CreateFlowerpotLinkCommand(resource.flowerpotCloudId());
    }
}
