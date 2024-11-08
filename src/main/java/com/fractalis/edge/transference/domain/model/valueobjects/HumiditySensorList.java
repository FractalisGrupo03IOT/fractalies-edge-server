package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

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
        Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000); // 10 minutos en milisegundos
        return humiditySensors.stream()
                .filter(sensor -> sensor.getDataDateTime().after(tenMinutesAgo))
                .collect(Collectors.toList());
    }
}
