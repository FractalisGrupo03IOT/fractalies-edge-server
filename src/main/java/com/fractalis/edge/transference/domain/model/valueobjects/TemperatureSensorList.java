package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class TemperatureSensorList {
    @OneToMany(mappedBy = "cropLink", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TemperatureSensor> temperatureSensors;

    public TemperatureSensorList()
    {
        this.temperatureSensors = new ArrayList<>();
    }

    public List<TemperatureSensor> getTemperatureSensors() {
        return temperatureSensors;
    }

    public void createTemperatureSensor(TemperatureSensor temperatureSensor)
    {
        this.temperatureSensors.add(temperatureSensor);
    }

    public void clear() {
        this.temperatureSensors.clear();
    }

    public List<TemperatureSensor> getRecentSensors() {
        // Obtener la hora actual en GMT-5
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Lima")); // Usar la zona horaria correcta
        ZonedDateTime tenMinutesAgo = now.minusMinutes(10); // Hora de hace 10 minutos

        return temperatureSensors.stream()
                .filter(sensor -> {
                    // Convierte el Date recuperado a ZonedDateTime en GMT-5
                    ZonedDateTime sensorDateTime = ZonedDateTime.ofInstant(sensor.getDataDateTime().toInstant(), ZoneId.of("America/Lima"));
                    // Compara las fechas
                    return sensorDateTime.isAfter(tenMinutesAgo);
                })
                .collect(Collectors.toList());
    }
}
