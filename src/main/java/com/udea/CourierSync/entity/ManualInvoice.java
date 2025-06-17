package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "manual_invoices")
@Data
public class ManualInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String shipmentReferenceId;

    @Column(nullable = false)
    private LocalDate emissionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    // Reutilizamos el mismo Enum 'InvoiceStatus' que ya tienes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus paymentStatus;

    @OneToMany(mappedBy = "manualInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManualPayment> payments;


    @PrePersist
    protected void onCreate() {
        // Asignar el estado PENDING por defecto al crear una nueva factura manual
        if (this.paymentStatus == null) {
            this.paymentStatus = InvoiceStatus.PENDING;
        }
    }
}