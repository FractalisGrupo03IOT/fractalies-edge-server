package com.fractalis.edge.transference.interfaces.rest.transform;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.interfaces.rest.resources.CropLinkResource;

public class CropLinkResourceFromEntityAssembler {
    public static CropLinkResource toResourceFromEntity(CropLink entity) {
        return new CropLinkResource(entity.getId(), entity.getCropCloudId(), entity.getIotDeviceId());
    }
}
