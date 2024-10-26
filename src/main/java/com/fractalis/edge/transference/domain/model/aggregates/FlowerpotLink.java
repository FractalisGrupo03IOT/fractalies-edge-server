package com.fractalis.edge.transference.domain.model.aggregates;

import com.fractalis.edge.transference.domain.model.entities.HumiditySensor;
import com.fractalis.edge.transference.domain.model.entities.SunlightSensor;
import com.fractalis.edge.transference.domain.model.entities.TemperatureSensor;
import com.fractalis.edge.transference.domain.model.valueobjects.HumiditySensorList;
import com.fractalis.edge.transference.domain.model.valueobjects.SunlightSensorList;
import com.fractalis.edge.transference.domain.model.valueobjects.TemperatureSensorList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class FlowerpotLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flowerpotCloudId;

    @Getter
    @Embedded
    private final TemperatureSensorList temperatureSensorList;

    @Getter
    @Embedded
    private final HumiditySensorList humiditySensorList;

    @Getter
    @Embedded
    private final SunlightSensorList sunlightSensorList;

    public FlowerpotLink() {
        this.flowerpotCloudId = null;
        this.temperatureSensorList = new TemperatureSensorList();
        this.humiditySensorList = new HumiditySensorList();
        this.sunlightSensorList = new SunlightSensorList();
    }

    public FlowerpotLink(Long flowerpotCloudId) {
        this();
        this.flowerpotCloudId = flowerpotCloudId;
    }

    public void createTemperatureSensor(FlowerpotLink  flowerpotLink, Long temperature) {
        TemperatureSensor temperatureSensor = new TemperatureSensor(this, temperature);
        this.temperatureSensorList.createTemperatureSensor(temperatureSensor);
    }

    public void createHumiditySensor(FlowerpotLink  flowerpotLink, Long humidity) {
        HumiditySensor humiditySensor = new HumiditySensor(this, humidity);
        this.humiditySensorList.createHumiditySensor(humiditySensor);
    }

    public void createSunlightSensor(FlowerpotLink  flowerpotLink, Long sunlight) {
        SunlightSensor sunlightSensor = new SunlightSensor(this, sunlight);
        this.sunlightSensorList.createSunlightSensor(sunlightSensor);
    }
}
