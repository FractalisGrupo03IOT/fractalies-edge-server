package com.fractalis.edge.transference.interfaces.rest;

import com.fractalis.edge.shared.domain.exceptions.ResourceNotFoundException;
import com.fractalis.edge.transference.domain.model.queries.GetFlowerpotLinkByIdQuery;
import com.fractalis.edge.transference.domain.services.FlowerpotLinkCommandService;
import com.fractalis.edge.transference.domain.services.FlowerpotLinkQueryService;
import com.fractalis.edge.transference.interfaces.rest.resources.*;
import com.fractalis.edge.transference.interfaces.rest.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/flowerpot/links")  // Ruta base del controlador
public class FlowerpotLinkController {

    private final FlowerpotLinkCommandService flowerpotLinkCommandService;
    private final FlowerpotLinkQueryService flowerpotLinkQueryService;

    @Autowired
    public FlowerpotLinkController(FlowerpotLinkCommandService flowerpotLinkCommandService,
                                   FlowerpotLinkQueryService flowerpotLinkQueryService) {
        this.flowerpotLinkCommandService = flowerpotLinkCommandService;
        this.flowerpotLinkQueryService = flowerpotLinkQueryService;
    }

    @PostMapping
    public ResponseEntity<FlowerpotLinkResource> createFlowerpotLink(@RequestBody CreateFlowerpotLinkResource resource) {
        var createFlowerpotLinkCommand = CreateFlowerpotLinkCommandFromResourceAssembler.toCommandFromResource(resource);
        var flowerpotLinkId = flowerpotLinkCommandService.handle(createFlowerpotLinkCommand);
        if (flowerpotLinkId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getFlowerpotLinkByIdQuery = new GetFlowerpotLinkByIdQuery(flowerpotLinkId);
        var flowerpotLink = flowerpotLinkQueryService.handle(getFlowerpotLinkByIdQuery);
        if (flowerpotLink.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var flowerpotLinkResource = FlowerpotLinkResourceFromEntityAssembler.toResourceFromEntity(flowerpotLink.get());
        return new ResponseEntity<>(flowerpotLinkResource, HttpStatus.CREATED);
    }

    @PostMapping("/temperature")
    public ResponseEntity<Void> createTemperatureSensor(@RequestBody CreateTemperatureSensorResource resource) {
        var createTemperatureSensorCommand = CreateTemperatureSensorCommandFromResourceAssembler.toCommandFromResource(resource);
        flowerpotLinkCommandService.handle(createTemperatureSensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/humidity")
    public ResponseEntity<Void> createHumiditySensor(@RequestBody CreateHumiditySensorResource resource) {
        var createHumiditySensorCommand = CreateHumiditySensorCommandFromResourceAssembler.toCommandFromResource(resource);
        flowerpotLinkCommandService.handle(createHumiditySensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sunlight")
    public ResponseEntity<Void> createSunlightSensor(@RequestBody CreateSunlightSensorResource resource) {
        var createSunlightSensorCommand = CreateSunlightSensorCommandFromResourceAssembler.toCommandFromResource(resource);
        flowerpotLinkCommandService.handle(createSunlightSensorCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{flowerpotLinkId}")
    public ResponseEntity<FlowerpotLinkResource> getFlowerpotLinkById(@PathVariable Long flowerpotLinkId) {
        var getFlowerpotLinkByIdQuery = new GetFlowerpotLinkByIdQuery(flowerpotLinkId);
        var flowerpotLink = flowerpotLinkQueryService.handle(getFlowerpotLinkByIdQuery);
        if (flowerpotLink.isEmpty()) {
            throw new ResourceNotFoundException("FlowerpotLink not found");
        }
        var flowerpotLinkResource = FlowerpotLinkResourceFromEntityAssembler.toResourceFromEntity(flowerpotLink.get());
        return new ResponseEntity<>(flowerpotLinkResource, HttpStatus.OK);
    }
}
