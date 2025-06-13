package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreateRouteDTO;
import com.udea.CourierSync.dto.RouteDTO;
import com.udea.CourierSync.entity.Route;
import com.udea.CourierSync.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "API para la gestión de Rutas")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea una nueva ruta en el sistema")
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody CreateRouteDTO createRouteDTO) {
        Route newRoute = routeService.createRoute(createRouteDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newRoute.getId())
                .toUri();

        RouteDTO routeDTO = RouteDTO.builder()
                .id(newRoute.getId())
                .origin(newRoute.getOrigin())
                .destination(newRoute.getDestination())
                .distance(newRoute.getDistance())
                .estimatedTime(newRoute.getEstimatedTime())
                .trafficLevel(newRoute.getTrafficLevel())
                .build();

        return ResponseEntity.created(location).body(routeDTO);
    }
}