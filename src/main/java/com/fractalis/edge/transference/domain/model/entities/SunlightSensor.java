package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SunlightSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Column(name = "data_date_time")
    private Date dataDateTime;

    @Getter
    private Long sunlight;

    public SunlightSensor(CropLink cropLink, Long sunlight) {
        this.cropLink = cropLink;
        this.sunlight = sunlight;
        this.dataDateTime = Date.from(Instant.now());
    }
}
