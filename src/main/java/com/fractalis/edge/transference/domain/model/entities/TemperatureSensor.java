package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flowerpot_link_id")
    private FlowerpotLink flowerpotLink;

    @Getter
    private Long temperature;

    public TemperatureSensor(FlowerpotLink flowerpotLink, Long temperature) {
        this.flowerpotLink = flowerpotLink;
        this.temperature = temperature;
    }
}
