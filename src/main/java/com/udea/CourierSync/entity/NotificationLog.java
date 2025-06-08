package com.udea.CourierSync.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
@Data
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoices_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private String type;

    @Lob // Para textos largos
    private String content;

    @Column(nullable = false)
    private String receiver;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @PrePersist
    protected void onCreate() {
        this.sendDate = LocalDateTime.now();
    }
}