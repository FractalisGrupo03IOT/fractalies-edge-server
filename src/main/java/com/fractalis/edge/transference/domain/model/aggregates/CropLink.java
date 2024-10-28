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
public class CropLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cropCloudId;

    @Getter
    @Embedded
    private final TemperatureSensorList temperatureSensorList;

    @Getter
    @Embedded
    private final HumiditySensorList humiditySensorList;

    @Getter
    @Embedded
    private final SunlightSensorList sunlightSensorList;

    public CropLink() {
        this.cropCloudId = null;
        this.temperatureSensorList = new TemperatureSensorList();
        this.humiditySensorList = new HumiditySensorList();
        this.sunlightSensorList = new SunlightSensorList();
    }

    public CropLink(Long cropCloudId) {
        this();
        this.cropCloudId = cropCloudId;
    }

    public void createTemperatureSensor(CropLink cropLink, Long temperature) {
        TemperatureSensor temperatureSensor = new TemperatureSensor(this, temperature);
        this.temperatureSensorList.createTemperatureSensor(temperatureSensor);
    }

    public void createHumiditySensor(CropLink cropLink, Long humidity) {
        HumiditySensor humiditySensor = new HumiditySensor(this, humidity);
        this.humiditySensorList.createHumiditySensor(humiditySensor);
    }

    public void createSunlightSensor(CropLink cropLink, Long sunlight) {
        SunlightSensor sunlightSensor = new SunlightSensor(this, sunlight);
        this.sunlightSensorList.createSunlightSensor(sunlightSensor);
    }
}
