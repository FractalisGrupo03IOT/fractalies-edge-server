package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

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
        Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000); // 10 minutos en milisegundos
        return sunlightSensors.stream()
                .filter(sensor -> sensor.getDataDateTime().after(tenMinutesAgo))
                .collect(Collectors.toList());
    }
}
