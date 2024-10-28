package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.domain.model.queries.GetCropLinkByIdQuery;

import java.util.Optional;

public interface CropLinkQueryService {
    Optional<CropLink> handle(GetCropLinkByIdQuery query);
}
