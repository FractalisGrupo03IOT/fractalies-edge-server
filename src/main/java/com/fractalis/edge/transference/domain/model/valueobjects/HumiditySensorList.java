package com.fractalis.edge.transference.domain.model.valueobjects;

import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class HumiditySensorList {
    @OneToMany(mappedBy = "cropLink", cascade = CascadeType.ALL, orphanRemoval = true)
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
}
