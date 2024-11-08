package com.fractalis.edge.transference.domain.services;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.domain.model.queries.GetCropByCloudIdQuery;
import com.fractalis.edge.transference.domain.model.queries.GetCropLinkByIdQuery;
import com.fractalis.edge.transference.domain.model.queries.GetCropsLinksWithoutIotDeviceQuery;

import java.util.List;
import java.util.Optional;

public interface CropLinkQueryService {
    Optional<CropLink> handle(GetCropLinkByIdQuery query);
    List<CropLink> handle(GetCropsLinksWithoutIotDeviceQuery query);
    Optional<CropLink> handle(GetCropByCloudIdQuery query);
}
