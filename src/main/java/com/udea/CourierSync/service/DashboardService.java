package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.FinancialDashboardDTO;
import com.udea.CourierSync.entity.Invoice;
import com.udea.CourierSync.entity.InvoiceStatus;
import com.udea.CourierSync.repository.InvoiceRepository;
import com.udea.CourierSync.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public FinancialDashboardDTO getFinancialSummary(int year, int month) {

        BigDecimal monthlyIncome = calculateMonthlyIncome(year, month);

        List<Invoice> pendingInvoices = invoiceRepository.findByPaymentStatusIn(
                Arrays.asList(InvoiceStatus.PENDING, InvoiceStatus.OVERDUE)
        );

        BigDecimal pendingAmount = BigDecimal.ZERO;
        for (Invoice invoice : pendingInvoices) {
            BigDecimal totalPaid = paymentService.getTotalPaidForInvoice(invoice.getId());
            pendingAmount = pendingAmount.add(invoice.getTotalAmount().subtract(totalPaid));
        }

        Double completionPercentage = calculateCompletionPercentage(year, month);

        return FinancialDashboardDTO.builder()
                .year(year)
                .month(month)
                .monthlyIncome(monthlyIncome)
                .pendingPaymentsAmount(pendingAmount)
                .totalPendingInvoices((long) pendingInvoices.size())
                .completionPercentage(completionPercentage)
                .build();
    }

    private BigDecimal calculateMonthlyIncome(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return paymentRepository.sumPaymentsInDateRange(startDate, endDate);
    }

    private Double calculateCompletionPercentage(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Invoice> invoicesInMonth = invoiceRepository.findByEmissionDateBetween(startDate, endDate);

        if (invoicesInMonth.isEmpty()) {
            return 100.0;
        }

        BigDecimal totalBilled = invoicesInMonth.stream()
                .map(Invoice::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaidForTheseInvoices = BigDecimal.ZERO;
        for (Invoice invoice : invoicesInMonth) {
            totalPaidForTheseInvoices = totalPaidForTheseInvoices.add(paymentService.getTotalPaidForInvoice(invoice.getId()));
        }

        if (totalBilled.compareTo(BigDecimal.ZERO) == 0) {
            return 100.0;
        }

        BigDecimal percentage = totalPaidForTheseInvoices.divide(totalBilled, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        return percentage.doubleValue();
    }
}