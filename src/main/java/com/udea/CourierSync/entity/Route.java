package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "route")
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    private Double distance;

    @Column(name = "estimated_time")
    private Integer estimatedTime; // en minutos

    @Column(name = "traffic_level")
    private String trafficLevel;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<Shipment> shipments;
}