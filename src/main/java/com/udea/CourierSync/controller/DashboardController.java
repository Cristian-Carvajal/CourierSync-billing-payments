package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.FinancialDashboardDTO;
import com.udea.CourierSync.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "API para la visualización de métricas de negocio")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/financial")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtiene un resumen financiero para un mes y año específicos")
    public ResponseEntity<FinancialDashboardDTO> getFinancialDashboard(
            @Parameter(description = "Año para la consulta. Si se omite, se usa el año actual.")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "Mes para la consulta (1-12). Si se omite, se usa el mes actual.")
            @RequestParam(required = false) Integer month) {

        LocalDate now = LocalDate.now();
        int queryYear = (year != null) ? year : now.getYear();
        int queryMonth = (month != null) ? month : now.getMonthValue();

        FinancialDashboardDTO dashboardData = dashboardService.getFinancialSummary(queryYear, queryMonth);

        dashboardData.add(linkTo(methodOn(DashboardController.class)
                .getFinancialDashboard(queryYear, queryMonth)).withSelfRel());

        YearMonth previousMonth = YearMonth.of(queryYear, queryMonth).minusMonths(1);
        dashboardData.add(linkTo(methodOn(DashboardController.class)
                .getFinancialDashboard(previousMonth.getYear(), previousMonth.getMonthValue())).withRel("previousMonth"));

        YearMonth nextMonth = YearMonth.of(queryYear, queryMonth).plusMonths(1);
        dashboardData.add(linkTo(methodOn(DashboardController.class)
                .getFinancialDashboard(nextMonth.getYear(), nextMonth.getMonthValue())).withRel("nextMonth"));

        return ResponseEntity.ok(dashboardData);
    }
}