package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreateManualInvoiceDTO;
import com.udea.CourierSync.entity.ManualInvoice;
import com.udea.CourierSync.repository.ManualInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ManualInvoiceService {

    private final ManualInvoiceRepository manualInvoiceRepository;

    @Transactional
    public ManualInvoice create(CreateManualInvoiceDTO dto) {
        ManualInvoice newInvoice = new ManualInvoice();
        newInvoice.setClientName(dto.getClientName());
        newInvoice.setShipmentReferenceId(dto.getShipmentReferenceId());
        newInvoice.setEmissionDate(dto.getEmissionDate());
        newInvoice.setAmount(dto.getAmount());

        return manualInvoiceRepository.save(newInvoice);
    }

    @Transactional(readOnly = true)
    public Page<ManualInvoice> findAll(Pageable pageable) {
        return manualInvoiceRepository.findAll(pageable);
    }
}