package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_payments")
@Data
public class ManualPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manual_invoice_id", nullable = false)
    private ManualInvoice manualInvoice;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentDate;

    //@PrePersist
    //protected void onCreate() {
        // Asignar la fecha actual por defecto al crear un nuevo pago
    //    if (this.paymentDate == null) {
    //        this.paymentDate = LocalDateTime.now();
    //    }
    //}
}