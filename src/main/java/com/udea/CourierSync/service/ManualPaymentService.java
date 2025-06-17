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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        System.out.println("====================");
        ManualInvoice invoice = manualInvoiceRepository.findById(dto.getManualInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura manual con id " + dto.getManualInvoiceId() + " no encontrada."));
        System.out.println(dto.getManualInvoiceId());
        validatePayment(dto, invoice);

        ManualPayment newPayment = new ManualPayment();
        newPayment.setManualInvoice(invoice);
        newPayment.setAmount(dto.getAmount());
        newPayment.setPaymentDate(dto.getPaymentDate());
        ManualPayment savedPayment = manualPaymentRepository.save(newPayment);

        return savedPayment;
    }

    private void validatePayment(CreateManualPaymentDTO paymentDto, ManualInvoice invoice) {
        if (invoice.getPaymentStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("La factura ya ha sido pagada en su totalidad.");
        }

        BigDecimal totalPaid = getTotalPaidForInvoice(invoice.getId());
        BigDecimal remainingAmount = invoice.getAmount().subtract(totalPaid);

        if (paymentDto.getAmount().compareTo(remainingAmount) > 0) {
            throw new BusinessException("El monto del pago (" + paymentDto.getAmount()
                    + ") excede el saldo pendiente (" + remainingAmount + ").");
        }
    }

    private void updateInvoiceStatus(ManualInvoice invoice) {
        BigDecimal totalPaid = getTotalPaidForInvoice(invoice.getId());

        if (totalPaid.compareTo(invoice.getAmount()) >= 0) {
            invoice.setPaymentStatus(InvoiceStatus.PAID);
        } else {
            invoice.setPaymentStatus(InvoiceStatus.PENDING);
        }
        manualInvoiceRepository.save(invoice);
    }

    private BigDecimal getTotalPaidForInvoice(Long invoiceId) {
        List<ManualPayment> existingPayments = manualPaymentRepository.findByManualInvoiceId(invoiceId);
        return existingPayments.stream()
                .map(ManualPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Page<ManualPayment> findAll(Pageable pageable) {
        return manualPaymentRepository.findAll(pageable);
    }
}