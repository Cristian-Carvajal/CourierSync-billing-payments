package com.udea.CourierSync.controller;

import com.udea.CourierSync.dto.CreateRouteDTO;
import com.udea.CourierSync.dto.RouteDTO;
import com.udea.CourierSync.entity.Route;
import com.udea.CourierSync.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "API para la gestión de Rutas")
@SecurityRequirement(name = "bearer-key")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea una nueva ruta en el sistema")
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody CreateRouteDTO createRouteDTO) {
        Route newRoute = routeService.createRoute(createRouteDTO);

        RouteDTO routeDTO = RouteDTO.builder()
                .id(newRoute.getId())
                .origin(newRoute.getOrigin())
                .destination(newRoute.getDestination())
                .distance(newRoute.getDistance())
                .estimatedTime(newRoute.getEstimatedTime())
                .trafficLevel(newRoute.getTrafficLevel())
                .build();

        routeDTO.add(linkTo(methodOn(ClientController.class).getClientById(newRoute.getId())).withSelfRel());

        URI location = URI.create(routeDTO.getLink("self").get().getHref());

        return ResponseEntity.created(location).body(routeDTO);
    }
}