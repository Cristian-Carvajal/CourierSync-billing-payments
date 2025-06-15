package com.udea.CourierSync.repository;

import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByClientId(Long clientId);

    List<Invoice> findByShipmentId(Long shipmentId);

    List<Invoice> findByPaymentStatus(InvoiceStatus status);

    List<Invoice> findByDueDateBeforeAndPaymentStatusNot(LocalDate date, InvoiceStatus status);

    List<Invoice> findByPaymentStatusIn(List<InvoiceStatus> statuses);

    List<Invoice> findByEmissionDateBetween(LocalDate startDate, LocalDate endDate);

}