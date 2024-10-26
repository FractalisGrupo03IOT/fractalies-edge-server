package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HumiditySensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flowerpot_link_id")
    private FlowerpotLink flowerpotLink;

    @Getter
    private Long humidity;

    public HumiditySensor(FlowerpotLink flowerpotLink, Long humidity) {
        this.flowerpotLink = flowerpotLink;
        this.humidity = humidity;
    }
}
