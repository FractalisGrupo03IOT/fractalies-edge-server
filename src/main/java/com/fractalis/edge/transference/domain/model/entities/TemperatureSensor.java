package com.fractalis.edge.transference.domain.model.entities;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TemperatureSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "crop_link_id")
    private CropLink cropLink;

    @Column(name = "data_date_time")
    private Date dataDateTime;

    @Getter
    private Long temperature;

    public TemperatureSensor(CropLink cropLink, Long temperature) {
        this.cropLink = cropLink;
        this.temperature = temperature;
        this.dataDateTime = new Date();
    }
}
