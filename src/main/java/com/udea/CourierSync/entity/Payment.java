package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Una factura puede tener muchos pagos
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice; // Relación directa con la entidad Invoice

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate; // Fecha y hora del pago

    @Column(nullable = false)
    private String paymentMethod; // Ej: "CREDIT_CARD", "BANK_TRANSFER"

    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now(); // La fecha de pago se establece al crear el registro
    }
}