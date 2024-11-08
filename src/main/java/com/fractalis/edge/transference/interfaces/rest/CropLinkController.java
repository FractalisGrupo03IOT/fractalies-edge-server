package com.fractalis.edge.transference.interfaces.rest;

import com.fractalis.edge.shared.domain.exceptions.ResourceNotFoundException;
import com.fractalis.edge.transference.domain.model.commands.InitializeIotDeviceCommand;
import com.fractalis.edge.transference.domain.model.commands.UnlinkIotDeviceCommand;
import com.fractalis.edge.transference.domain.model.queries.GetCropByCloudIdQuery;
import com.fractalis.edge.transference.domain.model.queries.GetCropLinkByIdQuery;
import com.fractalis.edge.transference.domain.model.queries.GetCropsLinksWithoutIotDeviceQuery;
import com.fractalis.edge.transference.domain.services.CropLinkCommandService;
import com.fractalis.edge.transference.domain.services.CropLinkQueryService;
import com.fractalis.edge.transference.interfaces.rest.resources.*;
import com.fractalis.edge.transference.interfaces.rest.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/crop/links")  // Ruta base del controlador
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

    @PutMapping("/initializeDevice/{cropCloudId}")
    public ResponseEntity<CropLinkResource> initializeDevice(@PathVariable("cropCloudId") Long cropCloudId,
                                                             @RequestParam String iotDeviceId) {
        var initializeIotDeviceCommand = new InitializeIotDeviceCommand(cropCloudId, iotDeviceId);
        cropLinkCommandService.handle(initializeIotDeviceCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/unlinkDevice/{cropCloudId}")
    public ResponseEntity<CropLinkResource> unlinkDevice(@PathVariable("cropCloudId") Long cropCloudId) {
        var unlinkIotDeviceCommand = new UnlinkIotDeviceCommand(cropCloudId);
        cropLinkCommandService.handle(unlinkIotDeviceCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/cropLinkId/{cropLinkId}")
    public ResponseEntity<CropLinkResource> getCropLinkById(@PathVariable Long cropLinkId) {
        var getCropLinkByIdQuery = new GetCropLinkByIdQuery(cropLinkId);
        var cropLink = cropLinkQueryService.handle(getCropLinkByIdQuery);
        if (cropLink.isEmpty()) {
            throw new ResourceNotFoundException("CropLink not found");
        }
        var CropLinkResource = CropLinkResourceFromEntityAssembler.toResourceFromEntity(cropLink.get());
        return new ResponseEntity<>(CropLinkResource, HttpStatus.OK);
    }

    @GetMapping("/cropCloudId/{cropCloudId}")
    public ResponseEntity<CropLinkResource> getCropByCloudId(@PathVariable Long cropCloudId) {
        var getCropLinkByIdQuery = new GetCropByCloudIdQuery(cropCloudId);
        var cropLink = cropLinkQueryService.handle(getCropLinkByIdQuery);
        if (cropLink.isEmpty()) {
            throw new ResourceNotFoundException("CropLink not found");
        }
        var CropLinkResource = CropLinkResourceFromEntityAssembler.toResourceFromEntity(cropLink.get());
        return new ResponseEntity<>(CropLinkResource, HttpStatus.OK);
    }

    @GetMapping("/cropsWithOutDevice")
    public ResponseEntity<List<CropLinkResource>> getCropsWithOutDevice() {
        var getCropsLinksWithOutDevice = new GetCropsLinksWithoutIotDeviceQuery();
        var cropsWithOutDevice = cropLinkQueryService.handle(getCropsLinksWithOutDevice);
        if (cropsWithOutDevice.isEmpty()) {
            throw new ResourceNotFoundException("All crops have assigned devices");
        }
        List<CropLinkResource> cropLinkResources = cropsWithOutDevice.stream().map(
                CropLinkResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(cropLinkResources, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 180000)
    public void sendPostForAllPlants(){
        this.cropLinkCommandService.sendToExternalService();
    }
}
