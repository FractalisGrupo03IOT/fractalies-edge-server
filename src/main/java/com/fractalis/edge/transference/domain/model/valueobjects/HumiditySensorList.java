package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
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
public class HumiditySensorList {
    @OneToMany(mappedBy = "cropLink", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<HumiditySensor> humiditySensors;

    public HumiditySensorList()
    {
        this.humiditySensors = new ArrayList<>();
    }

    public List<HumiditySensor> getHumiditySensors() {
        return humiditySensors;
    }

    public void createHumiditySensor(HumiditySensor humiditySensor)
    {
        this.humiditySensors.add(humiditySensor);
    }

    public void clear() {
        this.humiditySensors.clear();
    }

    public List<HumiditySensor> getRecentSensors() {
        // Obtener la hora actual en GMT-5
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Lima")); // Usar la zona horaria correcta
        ZonedDateTime tenMinutesAgo = now.minusMinutes(10); // Hora de hace 10 minutos

        return humiditySensors.stream()
                .filter(sensor -> {
                    // Convierte el Date recuperado a ZonedDateTime en GMT-5
                    ZonedDateTime sensorDateTime = ZonedDateTime.ofInstant(sensor.getDataDateTime().toInstant(), ZoneId.of("America/Lima"));
                    // Compara las fechas
                    return sensorDateTime.isAfter(tenMinutesAgo);
                })
                .collect(Collectors.toList());
    }
}
