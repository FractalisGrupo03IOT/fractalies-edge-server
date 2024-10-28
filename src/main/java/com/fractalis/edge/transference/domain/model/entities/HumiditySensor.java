package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
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
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Getter
    private Long humidity;

    public HumiditySensor(CropLink cropLink, Long humidity) {
        this.cropLink = cropLink;
        this.humidity = humidity;
    }
}
