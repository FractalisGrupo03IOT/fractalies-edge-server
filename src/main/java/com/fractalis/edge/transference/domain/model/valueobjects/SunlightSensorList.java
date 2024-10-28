package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class SunlightSensorList {
    @OneToMany(mappedBy = "cropLink", cascade = CascadeType.ALL, orphanRemoval = true)
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
}
