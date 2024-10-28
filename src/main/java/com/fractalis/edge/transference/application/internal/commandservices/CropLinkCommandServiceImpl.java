package com.fractalis.edge.transference.application.internal.commandservices;

import com.fractalis.edge.shared.domain.exceptions.ValidationException;
import com.fractalis.edge.shared.domain.resources.ExternalServiceRequest;
import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.domain.model.commands.CreateCropLinkCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateHumiditySensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateSunlightSensorCommand;
import com.fractalis.edge.transference.domain.model.commands.CreateTemperatureSensorCommand;
import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import com.fractalis.edge.transference.domain.services.CropLinkCommandService;
import com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories.CropLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CropLinkCommandServiceImpl implements CropLinkCommandService {

    private final CropLinkRepository cropLinkRepository;

    @Autowired
    public CropLinkCommandServiceImpl(CropLinkRepository cropLinkRepository) {
        this.cropLinkRepository = cropLinkRepository;
    }

    @Override
    public Long handle(CreateCropLinkCommand command) {
        if (command.cropCloudId().describeConstable().isEmpty()) {
            throw new ValidationException("Invalid cropCloudId");
        }
        if (command.cropCloudId() <= 0) {
            throw new ValidationException("Invalid cropCloudId");
        }
        if (cropLinkRepository.existsByCropCloudId(command.cropCloudId())) {
            throw new ValidationException("FlowerpotLink already exists");
        }

        CropLink cropLink = new CropLink(command.cropCloudId());
        return cropLinkRepository.save(cropLink).getId();
    }

    @Override
    public void handle(CreateTemperatureSensorCommand command) {
        CropLink cropLink = cropLinkRepository.findByCropCloudId(command.cropCloudId())
                .orElseGet(() -> {
                    CropLink newCropLink = new CropLink(command.cropCloudId());
                    return cropLinkRepository.save(newCropLink);
                });

        cropLink.createTemperatureSensor(cropLink, command.temperature());
        cropLinkRepository.save(cropLink);

        if (cropLink.getTemperatureSensorList().getTemperatureSensors().size() >= 10) {
            double averageTemperature = calculateAverageTemperature(cropLink.getTemperatureSensorList().getTemperatureSensors());
            sendToExternalService(cropLink.getCropCloudId(), 1, averageTemperature);
            cropLink.getTemperatureSensorList().clear();
            cropLinkRepository.save(cropLink);
        }
    }

    @Override
    public void handle(CreateHumiditySensorCommand command) {
        CropLink cropLink = cropLinkRepository.findByCropCloudId(command.cropCloudId())
                .orElseGet(() -> {
                    CropLink newCropLink = new CropLink(command.cropCloudId());
                    return cropLinkRepository.save(newCropLink);
                });

        cropLink.createHumiditySensor(cropLink, command.humidity());
        cropLinkRepository.save(cropLink);

        if (cropLink.getHumiditySensorList().getHumiditySensors().size() >= 10) {
            double averageHumidity = calculateAverageHumidity(cropLink.getHumiditySensorList().getHumiditySensors());
            sendToExternalService(cropLink.getCropCloudId(), 2, averageHumidity);
            cropLink.getHumiditySensorList().clear();
            cropLinkRepository.save(cropLink);
        }
    }

    @Override
    public void handle(CreateSunlightSensorCommand command) {
        CropLink cropLink = cropLinkRepository.findByCropCloudId(command.cropCloudId())
                .orElseGet(() -> {
                    CropLink newCropLink = new CropLink(command.cropCloudId());
                    return cropLinkRepository.save(newCropLink);
                });

        cropLink.createSunlightSensor(cropLink, command.sunlight());
        cropLinkRepository.save(cropLink);

        if (cropLink.getSunlightSensorList().getSunlightSensors().size() >= 10) {
            double averageSunlight = calculateAverageSunlight(cropLink.getSunlightSensorList().getSunlightSensors());
            sendToExternalService(cropLink.getCropCloudId(), 3, averageSunlight);
            cropLink.getSunlightSensorList().clear();
            cropLinkRepository.save(cropLink);
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
