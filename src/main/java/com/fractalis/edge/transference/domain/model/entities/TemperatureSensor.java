package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
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
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Getter
    private Long temperature;

    public TemperatureSensor(CropLink cropLink, Long temperature) {
        this.cropLink = cropLink;
        this.temperature = temperature;
    }
}
