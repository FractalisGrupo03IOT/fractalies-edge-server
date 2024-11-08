package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
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
public class SunlightSensorList {
    @OneToMany(mappedBy = "cropLink", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SunlightSensor> sunlightSensors;

    public SunlightSensorList()
    {
        this.sunlightSensors = new ArrayList<>();
    }

    public List<SunlightSensor> getSunlightSensors() {
        return sunlightSensors;
    }

    public void createSunlightSensor(SunlightSensor sunlightSensor)
    {
        this.sunlightSensors.add(sunlightSensor);
    }

    public void clear() {
        this.sunlightSensors.clear();
    }

    public List<SunlightSensor> getRecentSensors() {
        // Obtener la hora actual en GMT-5
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Lima")); // Usar la zona horaria correcta
        ZonedDateTime tenMinutesAgo = now.minusMinutes(10); // Hora de hace 10 minutos

        return sunlightSensors.stream()
                .filter(sensor -> {
                    // Convierte el Date recuperado a ZonedDateTime en GMT-5
                    ZonedDateTime sensorDateTime = ZonedDateTime.ofInstant(sensor.getDataDateTime().toInstant(), ZoneId.of("America/Lima"));
                    // Compara las fechas
                    return sensorDateTime.isAfter(tenMinutesAgo);
                })
                .collect(Collectors.toList());
    }
}
