package com.fractalis.edge.transference.application.internal.commandservices;

import com.fractalis.edge.shared.domain.exceptions.ValidationException;
import com.fractalis.edge.shared.domain.resources.ExternalServiceRequest;
import com.fractalis.edge.shared.domain.resources.PlantStatusRequest;
import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import com.fractalis.edge.transference.domain.model.commands.*;
import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import com.fractalis.edge.transference.domain.services.CropLinkCommandService;
import com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories.CropLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        CropLink cropLink = cropLinkRepository.findByIotDeviceId(command.iotDeviceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe un Crop con el ID de dispositivo IoT: " + command.iotDeviceId())
                );

        cropLink.createTemperatureSensor(cropLink, command.temperature());
        cropLinkRepository.save(cropLink);

        if (cropLink.getTemperatureSensorList().getTemperatureSensors().size() >= 10) {
            //double averageTemperature = calculateAverageTemperature(cropLink.getTemperatureSensorList().getTemperatureSensors());
            //sendToExternalService(cropLink.getCropCloudId(), 1, averageTemperature);
            cropLink.getTemperatureSensorList().clear();
            cropLinkRepository.save(cropLink);
        }
    }

    @Override
    public void handle(CreateHumiditySensorCommand command) {
        CropLink cropLink = cropLinkRepository.findByIotDeviceId(command.iotDeviceId())
                .orElseThrow(() ->
                    new IllegalArgumentException("No existe un Crop con el ID de dispositivo IoT: " + command.iotDeviceId())
                );

        cropLink.createHumiditySensor(cropLink, command.humidity());
        cropLinkRepository.save(cropLink);

        if (cropLink.getHumiditySensorList().getHumiditySensors().size() >= 10) {
            //double averageHumidity = calculateAverageHumidity(cropLink.getHumiditySensorList().getHumiditySensors());
            //sendToExternalService(cropLink.getCropCloudId(), 2, averageHumidity);
            cropLink.getHumiditySensorList().clear();
            cropLinkRepository.save(cropLink);
        }
    }

    @Override
    public void handle(CreateSunlightSensorCommand command) {
        CropLink cropLink = cropLinkRepository.findByIotDeviceId(command.iotDeviceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe un Crop con el ID de dispositivo IoT: " + command.iotDeviceId())
                );

        cropLink.createSunlightSensor(cropLink, command.sunlight());
        cropLinkRepository.save(cropLink);

        if (cropLink.getSunlightSensorList().getSunlightSensors().size() >= 10) {
            //double averageSunlight = calculateAverageSunlight(cropLink.getSunlightSensorList().getSunlightSensors());
            //sendToExternalService(cropLink.getCropCloudId(), 3, averageSunlight);
            cropLink.getSunlightSensorList().clear();
            cropLinkRepository.save(cropLink);
        }
    }

    @Override
    public void handle(InitializeIotDeviceCommand command) {
        CropLink cropLink = cropLinkRepository.findByCropCloudId(command.cropId())
                .orElseThrow(() -> new IllegalArgumentException("CropLink not found"));

        cropLink.initializeIotDevice(command.iotDeviceId());
        cropLinkRepository.save(cropLink);

    }

    @Override
    public void handle(UnlinkIotDeviceCommand command) {
        CropLink cropLink = cropLinkRepository.findByCropCloudId(command.CropCloudId())
                .orElseThrow(() -> new IllegalArgumentException("CropLink not found"));

        cropLink.unlinkIotDevice();
        cropLinkRepository.save(cropLink);
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

    @Override
    public void sendToExternalService() {
        String url = "https://fractalisbackend-production.up.railway.app/api/v1/cropData";
        RestTemplate restTemplate = new RestTemplate();

        List<CropLink> cropLinks = cropLinkRepository.findCropLinksByIotDeviceIdIsNotNull();

        for (CropLink cropLink : cropLinks) {
            List<HumiditySensor> recentHumiditySensors = cropLink.getHumiditySensorList().getRecentSensors();
            List<TemperatureSensor> recentTemperatureSensors = cropLink.getTemperatureSensorList().getRecentSensors();
            List<SunlightSensor> recentSunlightSensors = cropLink.getSunlightSensorList().getRecentSensors();

            if (recentHumiditySensors.isEmpty() || recentTemperatureSensors.isEmpty() || recentSunlightSensors.isEmpty()) {
                System.out.println("Una de las listas está vacía, saltando cropLink ID: " + cropLink.getCropCloudId());
                continue; // Salta al siguiente cropLink
            }

            // Obtener el promedio de la lista
            double humidity = calculateAverageHumidity(recentHumiditySensors);
            double temperature = calculateAverageTemperature(recentTemperatureSensors);
            double uv = calculateAverageSunlight(recentSunlightSensors);

            // Crear el cuerpo de la petición
            PlantStatusRequest request = new PlantStatusRequest(cropLink.getCropCloudId(), humidity, temperature, uv);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Crear HttpEntity con el cuerpo y encabezados
            HttpEntity<PlantStatusRequest> requestEntity = new HttpEntity<>(request, headers);

            try {
                restTemplate.postForObject(url, requestEntity, String.class);
                System.out.println("POST enviado con éxito para cropLink ID: " + cropLink.getCropCloudId());
            } catch (Exception e) {
                System.err.println("Error al enviar el POST para cropLink ID: " + cropLink.getCropCloudId() + ": " + e.getMessage());
            }
        }
    }
}