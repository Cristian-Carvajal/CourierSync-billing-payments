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
    @JoinColumn(name = "manual_invoice_id", nullable = true)
    private ManualInvoice manualInvoice;

    @Column(nullable = true)
    private BigDecimal amount;

    @Column(nullable = true)
    private String paymentMethod;

    @Column(nullable = true)
    private String paymentDate;
}