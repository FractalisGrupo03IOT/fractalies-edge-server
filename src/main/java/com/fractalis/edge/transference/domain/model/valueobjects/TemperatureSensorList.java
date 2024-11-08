package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

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
        Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000); // 10 minutos en milisegundos
        return temperatureSensors.stream()
                .filter(sensor -> sensor.getDataDateTime().after(tenMinutesAgo))
                .collect(Collectors.toList());
    }
}
