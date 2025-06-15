package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@Builder
public class FinancialDashboardDTO extends RepresentationModel<FinancialDashboardDTO> {

    private int year;
    private int month;
    private BigDecimal monthlyIncome;
    private BigDecimal pendingPaymentsAmount;
    private Long totalPendingInvoices;
    private Double completionPercentage;
}