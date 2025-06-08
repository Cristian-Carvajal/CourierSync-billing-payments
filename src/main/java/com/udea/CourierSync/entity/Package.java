package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "package")
@Data
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(nullable = false)
    private String code;

    private String description;

    private Double weight;

    private String status;

    @OneToMany(mappedBy = "aPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PackageLocation> locationHistory;
}