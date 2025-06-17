package com.udea.CourierSync.repository;

import com.udea.CourierSync.entity.ManualInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManualInvoiceRepository extends JpaRepository<ManualInvoice, Long> {
}