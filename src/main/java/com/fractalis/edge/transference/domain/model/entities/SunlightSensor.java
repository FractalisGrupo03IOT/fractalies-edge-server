package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SunlightSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Getter
    private Long sunlight;

    public SunlightSensor(CropLink cropLink, Long sunlight) {
        this.cropLink = cropLink;
        this.sunlight = sunlight;
    }
}
