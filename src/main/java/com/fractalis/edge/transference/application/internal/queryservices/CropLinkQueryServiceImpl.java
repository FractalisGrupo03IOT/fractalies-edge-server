package com.fractalis.edge.transference.application.internal.queryservices;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.domain.model.queries.GetCropLinkByIdQuery;
import com.fractalis.edge.transference.domain.services.CropLinkQueryService;
import com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories.CropLinkRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CropLinkQueryServiceImpl implements CropLinkQueryService {

    private final CropLinkRepository cropLinkRepository;

    public CropLinkQueryServiceImpl(CropLinkRepository cropLinkRepository) {
        this.cropLinkRepository = cropLinkRepository;
    }

    @Override
    public Optional<CropLink> handle(GetCropLinkByIdQuery query) {
        return cropLinkRepository.findById(query.cropLinkId());
    }
}
