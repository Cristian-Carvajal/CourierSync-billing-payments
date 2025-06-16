package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreatePaymentDTO;
import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.InvoiceStatus;
import com.udea.CourierSync.entity.Payment;
import com.udea.CourierSync.exception.BusinessException;
import com.udea.CourierSync.exception.ResourceNotFoundException;
import com.udea.CourierSync.repository.InvoiceRepository;
import com.udea.CourierSync.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public Payment createPayment(CreatePaymentDTO paymentDTO) {
        Invoice invoice = invoiceRepository.findById(paymentDTO.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Factura con id: " + paymentDTO.getInvoiceId() + " no encontrada"));

        validatePayment(paymentDTO, invoice);

        Payment newPayment = new Payment();
        newPayment.setInvoice(invoice);
        newPayment.setAmount(paymentDTO.getAmount());
        newPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        newPayment.setPaymentDate(paymentDTO.getPaymentDate() != null ?
                paymentDTO.getPaymentDate().atStartOfDay() :
                LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(newPayment);

        updateInvoiceStatus(invoice);

        return savedPayment;
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con id: " + id + " no encontrado"));
    }

    private void validatePayment(CreatePaymentDTO paymentDTO, Invoice invoice) {
        if (invoice.getPaymentStatus() == InvoiceStatus.PAID) {
            throw new BusinessException("La factura ya ha sido pagada en su totalidad.");
        }

        BigDecimal totalPaid = getTotalPaidForInvoice(invoice.getId());
        BigDecimal totalOwed = invoice.getTotalAmount();
        BigDecimal remainingAmount = totalOwed.subtract(totalPaid);

        if (paymentDTO.getAmount().compareTo(remainingAmount) > 0) {
            throw new BusinessException("El monto del pago (" + paymentDTO.getAmount() +
                    ") excede el saldo pendiente (" + remainingAmount + ").");
        }
    }

    private void updateInvoiceStatus(Invoice invoice) {
        BigDecimal totalPaid = getTotalPaidForInvoice(invoice.getId());
        BigDecimal totalOwed = invoice.getTotalAmount();

        if (totalPaid.compareTo(totalOwed) >= 0) {
            invoice.setPaymentStatus(InvoiceStatus.PAID);
        } else {
            invoice.setPaymentStatus(InvoiceStatus.PENDING);
        }
        invoiceRepository.save(invoice);
    }

    public BigDecimal getTotalPaidForInvoice(Long invoiceId) {
        List<Payment> existingPayments = paymentRepository.findByInvoiceId(invoiceId);
        return existingPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
}