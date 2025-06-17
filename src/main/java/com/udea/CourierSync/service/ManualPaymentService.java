package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreateManualPaymentDTO;
import com.udea.CourierSync.entity.InvoiceStatus;
import com.udea.CourierSync.entity.ManualInvoice;
import com.udea.CourierSync.entity.ManualPayment;
import com.udea.CourierSync.exception.BusinessException;
import com.udea.CourierSync.exception.ResourceNotFoundException;
import com.udea.CourierSync.repository.ManualInvoiceRepository;
import com.udea.CourierSync.repository.ManualPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManualPaymentService {

    private final ManualPaymentRepository manualPaymentRepository;
    private final ManualInvoiceRepository manualInvoiceRepository;

    @Transactional
    public ManualPayment createPayment(CreateManualPaymentDTO dto) {
        // 1. Encontrar la factura manual
        ManualInvoice invoice = manualInvoiceRepository.findById(dto.getManualInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura manual con id " + dto.getManualInvoiceId() + " no encontrada."));

        // 2. Crear y guardar el nuevo pago
        ManualPayment newPayment = new ManualPayment();
        newPayment.setManualInvoice(invoice);
        newPayment.setAmount(dto.getAmount());
        newPayment.setPaymentDate(dto.getPaymentDate()); // Asignación directa de String
        ManualPayment savedPayment = manualPaymentRepository.save(newPayment);

        // 3. Convertir el String de estado a Enum y actualizar la factura
        try {
            InvoiceStatus status = InvoiceStatus.valueOf(dto.getNewStatus().toUpperCase());
            invoice.setPaymentStatus(status);
            manualInvoiceRepository.save(invoice);
        } catch (IllegalArgumentException e) {
            // Esto no debería pasar gracias a la validación @Pattern, pero es una defensa extra.
            throw new BusinessException("Estado '" + dto.getNewStatus() + "' inválido.");
        }

        return savedPayment;
    }

    @Transactional(readOnly = true) // Marcar como de solo lectura optimiza la consulta
    public Page<ManualPayment> findAll(Pageable pageable) {
        return manualPaymentRepository.findAll(pageable);
    }

    // El resto de los métodos (findAll, etc.) no necesitan cambios
}