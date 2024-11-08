package com.fractalis.edge.shared.domain.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantStatusRequest {
    @JsonProperty("plantId")
    private Long plantId;
    @JsonProperty("humidity")
    private double humidity;
    @JsonProperty("temperature")
    private double temperature;
    @JsonProperty("uv")
    private double uv;

    public PlantStatusRequest(Long plantId, double humidity, double temperature, double uv) {
        this.plantId = plantId;
        this.humidity = humidity;
        this.temperature = temperature;
        this.uv = uv;
    }
}
