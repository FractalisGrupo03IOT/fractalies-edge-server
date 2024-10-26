package com.fractalis.edge.shared.domain.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalServiceRequest {
    @JsonProperty("flowerpotId")
    private Long flowerpotId;
    @JsonProperty("type")
    private int type;
    @JsonProperty("value")
    private double value;

    public ExternalServiceRequest(Long flowerpotId, int type, double value) {
        this.flowerpotId = flowerpotId;
        this.type = type;
        this.value = value;
    }

    // Getters and setters
    public Long getFlowerpotId() { return flowerpotId; }
    public void setFlowerpotId(Long flowerpotId) { this.flowerpotId = flowerpotId; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}