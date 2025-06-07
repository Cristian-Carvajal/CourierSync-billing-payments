package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shipmentId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDate emissionDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING) // Guardar el nombre del enum ("PAID") en la BD, no el número (ordinal). Es más seguro y legible.
    @Column(nullable = false)
    private InvoiceStatus status;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.emissionDate = LocalDate.now(); // La fecha de emisión se establece al crear
        if (this.dueDate == null) {
            this.dueDate = LocalDate.now().plusDays(30); // Vencimiento por defecto a 30 días si no se especifica
        }
        if (this.status == null) {
            this.status = InvoiceStatus.PENDING; // Estado inicial por defecto
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}