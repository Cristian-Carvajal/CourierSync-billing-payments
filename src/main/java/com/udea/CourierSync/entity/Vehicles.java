package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    private String type;

    @Column(name = "weight_capacity", nullable = false)
    private Double weightCapacity;

    @Column(name = "fuel_consumption")
    private Double fuelConsumption;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<RouteAssignment> routeAssignments;
}