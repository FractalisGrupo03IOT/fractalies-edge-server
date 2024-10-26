package com.fractalis.edge.transference.application.internal.commandservices;

import com.fractalis.edge.shared.domain.exceptions.ValidationException;
import com.fractalis.edge.shared.domain.resources.ExternalServiceRequest;
import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import com.fractalis.edge.transference.domain.model.commands.CreateFlowerpotLinkCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateHumiditySensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateSunlightSensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateTemperatureSensorCommand;
import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import com.fractalis.edge.transference.domain.services.FlowerpotLinkCommandService;
import com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories.FlowerpotLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FlowerpotLinkCommandServiceImpl implements FlowerpotLinkCommandService {

    private final FlowerpotLinkRepository flowerpotLinkRepository;

    @Autowired
    public FlowerpotLinkCommandServiceImpl(FlowerpotLinkRepository flowerpotLinkRepository) {
        this.flowerpotLinkRepository = flowerpotLinkRepository;
    }

    @Override
    public Long handle(CreateFlowerpotLinkCommand command) {
        if (command.flowerpotCloudId().describeConstable().isEmpty()) {
            throw new ValidationException("Invalid flowerpotCloudId");
        }
        if (command.flowerpotCloudId() <= 0) {
            throw new ValidationException("Invalid flowerpotCloudId");
        }
        if (flowerpotLinkRepository.existsByFlowerpotCloudId(command.flowerpotCloudId())) {
            throw new ValidationException("FlowerpotLink already exists");
        }

        FlowerpotLink flowerpotLink = new FlowerpotLink(command.flowerpotCloudId());
        return flowerpotLinkRepository.save(flowerpotLink).getId();
    }

    @Override
    public void handle(CreateTemperatureSensorCommand command) {
        FlowerpotLink flowerpotLink = flowerpotLinkRepository.findByFlowerpotCloudId(command.flowerpotCloudId())
                .orElseGet(() -> {
                    FlowerpotLink newFlowerpotLink = new FlowerpotLink(command.flowerpotCloudId());
                    return flowerpotLinkRepository.save(newFlowerpotLink);
                });

        flowerpotLink.createTemperatureSensor(flowerpotLink, command.temperature());
        flowerpotLinkRepository.save(flowerpotLink);

        if (flowerpotLink.getTemperatureSensorList().getTemperatureSensors().size() >= 10) {
            double averageTemperature = calculateAverageTemperature(flowerpotLink.getTemperatureSensorList().getTemperatureSensors());
            sendToExternalService(flowerpotLink.getFlowerpotCloudId(), 1, averageTemperature);
            flowerpotLink.getTemperatureSensorList().clear();
            flowerpotLinkRepository.save(flowerpotLink);
        }
    }

    @Override
    public void handle(CreateHumiditySensorCommand command) {
        FlowerpotLink flowerpotLink = flowerpotLinkRepository.findByFlowerpotCloudId(command.flowerpotCloudId())
                .orElseGet(() -> {
                    FlowerpotLink newFlowerpotLink = new FlowerpotLink(command.flowerpotCloudId());
                    return flowerpotLinkRepository.save(newFlowerpotLink);
                });

        flowerpotLink.createHumiditySensor(flowerpotLink, command.humidity());
        flowerpotLinkRepository.save(flowerpotLink);

        if (flowerpotLink.getHumiditySensorList().getHumiditySensors().size() >= 10) {
            double averageHumidity = calculateAverageHumidity(flowerpotLink.getHumiditySensorList().getHumiditySensors());
            sendToExternalService(flowerpotLink.getFlowerpotCloudId(), 2, averageHumidity);
            flowerpotLink.getHumiditySensorList().clear();
            flowerpotLinkRepository.save(flowerpotLink);
        }
    }

    @Override
    public void handle(CreateSunlightSensorCommand command) {
        FlowerpotLink flowerpotLink = flowerpotLinkRepository.findByFlowerpotCloudId(command.flowerpotCloudId())
                .orElseGet(() -> {
                    FlowerpotLink newFlowerpotLink = new FlowerpotLink(command.flowerpotCloudId());
                    return flowerpotLinkRepository.save(newFlowerpotLink);
                });

        flowerpotLink.createSunlightSensor(flowerpotLink, command.sunlight());
        flowerpotLinkRepository.save(flowerpotLink);

        if (flowerpotLink.getSunlightSensorList().getSunlightSensors().size() >= 10) {
            double averageSunlight = calculateAverageSunlight(flowerpotLink.getSunlightSensorList().getSunlightSensors());
            sendToExternalService(flowerpotLink.getFlowerpotCloudId(), 3, averageSunlight);
            flowerpotLink.getSunlightSensorList().clear();
            flowerpotLinkRepository.save(flowerpotLink);
        }
    }

    private double calculateAverageTemperature(List<TemperatureSensor> sensors) {
        return sensors.stream().mapToDouble(TemperatureSensor::getTemperature).average().orElse(0.0);
    }

    private double calculateAverageHumidity(List<HumiditySensor> sensors) {
        return sensors.stream().mapToDouble(HumiditySensor::getHumidity).average().orElse(0.0);
    }

    private double calculateAverageSunlight(List<SunlightSensor> sensors) {
        return sensors.stream().mapToDouble(SunlightSensor::getSunlight).average().orElse(0.0);
    }

    private void sendToExternalService(Long flowerpotId, int type, double value) {
        String url = "https://ztech-web-service-production.up.railway.app/api/v1/flowerpots/sensors";
        RestTemplate restTemplate = new RestTemplate();

        ExternalServiceRequest request = new ExternalServiceRequest(flowerpotId, type, value);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ExternalServiceRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send data to external service");
        }
    }
}
