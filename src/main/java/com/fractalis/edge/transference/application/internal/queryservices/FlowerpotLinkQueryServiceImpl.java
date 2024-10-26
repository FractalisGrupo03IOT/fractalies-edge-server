package com.fractalis.edge.transference.application.internal.queryservices;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import com.fractalis.edge.transference.domain.model.queries.GetFlowerpotLinkByIdQuery;
import com.fractalis.edge.transference.domain.services.FlowerpotLinkQueryService;
import com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories.FlowerpotLinkRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlowerpotLinkQueryServiceImpl implements FlowerpotLinkQueryService {

    private final FlowerpotLinkRepository flowerpotLinkRepository;

    public FlowerpotLinkQueryServiceImpl(FlowerpotLinkRepository flowerpotLinkRepository) {
        this.flowerpotLinkRepository = flowerpotLinkRepository;
    }

    @Override
    public Optional<FlowerpotLink> handle(GetFlowerpotLinkByIdQuery query) {
        return flowerpotLinkRepository.findById(query.flowerpotLinkId());
    }
}
