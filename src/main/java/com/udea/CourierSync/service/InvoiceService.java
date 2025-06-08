package com.udea.CourierSync.service;

import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.Shipment;
import com.udea.CourierSync.exception.ResourceNotFoundException;
import com.udea.CourierSync.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    // Constantes para el cálculo del costo (pueden venir de la BD o un config file)
    private static final BigDecimal COST_PER_KG = new BigDecimal("2500");
    private static final BigDecimal BASE_FEE = new BigDecimal("5000");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.19"); // 19% IVA

    // Se utiliza para mantener consistencia en la base de datos en caso de algun error
    @Transactional
    public Invoice createInvoiceForShipment(Shipment shipment) {
        BigDecimal totalAmount = calculateTotalAmount(shipment);

        Invoice invoice = new Invoice();
        invoice.setShipment(shipment);
        invoice.setClient(shipment.getClient());
        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id: " + id));
    }

    private BigDecimal calculateTotalAmount(Shipment shipment) {
        BigDecimal weightCost = COST_PER_KG.multiply(BigDecimal.valueOf(shipment.getWeight()));
        BigDecimal subtotal = weightCost.add(BASE_FEE);
        BigDecimal taxAmount = subtotal.multiply(TAX_RATE);
        BigDecimal total = subtotal.add(taxAmount);

        // Redondear a 2 decimales
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}