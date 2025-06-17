package com.udea.CourierSync.repository;

import com.udea.CourierSync.entity.ManualPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualPaymentRepository extends JpaRepository<ManualPayment, Long> {
    List<ManualPayment> findByManualInvoiceId(Long manualInvoiceId);
}