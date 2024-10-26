package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import com.fractalis.edge.transference.domain.model.queries.GetFlowerpotLinkByIdQuery;

import java.util.Optional;

public interface FlowerpotLinkQueryService {
    Optional<FlowerpotLink> handle(GetFlowerpotLinkByIdQuery query);
}
