package com.fractalis.edge.transference.interfaces.rest;

import com.fractalis.edge.shared.domain.exceptions.ResourceNotFoundException;
import com.fractalis.edge.transference.domain.model.queries.GetCropLinkByIdQuery;
import com.fractalis.edge.transference.domain.services.CropLinkCommandService;
import com.fractalis.edge.transference.domain.services.CropLinkQueryService;
import com.fractalis.edge.transference.interfaces.rest.resources.*;
import com.fractalis.edge.transference.interfaces.rest.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/flowerpot/links")  // Ruta base del controlador
public class CropLinkController {

    private final CropLinkCommandService cropLinkCommandService;
    private final CropLinkQueryService cropLinkQueryService;

    @Autowired
    public CropLinkController(CropLinkCommandService cropLinkCommandService,
                              CropLinkQueryService cropLinkQueryService) {
        this.cropLinkCommandService = cropLinkCommandService;
        this.cropLinkQueryService = cropLinkQueryService;
    }

    @PostMapping
    public ResponseEntity<CropLinkResource> createFlowerpotLink(@RequestBody CreateCropLinkResource resource) {
        var createCropLinkCommand = CreateCropLinkCommandFromResourceAssembler.toCommandFromResource(resource);
        var cropLinkId = cropLinkCommandService.handle(createCropLinkCommand);
        if (cropLinkId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getCropLinkByIdQuery = new GetCropLinkByIdQuery(cropLinkId);
        var cropLink = cropLinkQueryService.handle(getCropLinkByIdQuery);
        if (cropLink.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var cropLinkResource = CropLinkResourceFromEntityAssembler.toResourceFromEntity(cropLink.get());
        return new ResponseEntity<>(cropLinkResource, HttpStatus.CREATED);
    }

    @PostMapping("/temperature")
    public ResponseEntity<Void> createTemperatureSensor(@RequestBody CreateTemperatureSensorResource resource) {
        var createTemperatureSensorCommand = CreateTemperatureSensorCommandFromResourceAssembler.toCommandFromResource(resource);
        cropLinkCommandService.handle(createTemperatureSensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/humidity")
    public ResponseEntity<Void> createHumiditySensor(@RequestBody CreateHumiditySensorResource resource) {
        var createHumiditySensorCommand = CreateHumiditySensorCommandFromResourceAssembler.toCommandFromResource(resource);
        cropLinkCommandService.handle(createHumiditySensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sunlight")
    public ResponseEntity<Void> createSunlightSensor(@RequestBody CreateSunlightSensorResource resource) {
        var createSunlightSensorCommand = CreateSunlightSensorCommandFromResourceAssembler.toCommandFromResource(resource);
        cropLinkCommandService.handle(createSunlightSensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{cropLinkId}")
    public ResponseEntity<CropLinkResource> getFlowerpotLinkById(@PathVariable Long cropLinkId) {
        var getCropLinkByIdQuery = new GetCropLinkByIdQuery(cropLinkId);
        var cropLink = cropLinkQueryService.handle(getCropLinkByIdQuery);
        if (cropLink.isEmpty()) {
            throw new ResourceNotFoundException("CropLink not found");
        }
        var CropLinkResource = CropLinkResourceFromEntityAssembler.toResourceFromEntity(cropLink.get());
        return new ResponseEntity<>(CropLinkResource, HttpStatus.OK);
    }
}
