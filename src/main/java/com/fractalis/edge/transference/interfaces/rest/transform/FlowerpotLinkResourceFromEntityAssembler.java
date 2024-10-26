package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import com.fractalis.edge.transference.interfaces.rest.resources.FlowerpotLinkResource;

public class FlowerpotLinkResourceFromEntityAssembler {
    public static FlowerpotLinkResource toResourceFromEntity(FlowerpotLink entity) {
        return new FlowerpotLinkResource(entity.getId(), entity.getFlowerpotCloudId());
    }
}
