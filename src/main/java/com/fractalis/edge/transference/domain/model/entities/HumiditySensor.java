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
public class HumiditySensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Column(name = "data_date_time")
    private Date dataDateTime;

    @Getter
    private Long humidity;

    public HumiditySensor(CropLink cropLink, Long humidity) {
        this.cropLink = cropLink;
        this.humidity = humidity;
        this.dataDateTime = Date.from(Instant.now());
    }
}
