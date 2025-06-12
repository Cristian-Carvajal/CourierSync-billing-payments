package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreateRouteDTO;
import com.udea.CourierSync.entity.Route;
import com.udea.CourierSync.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route createRoute(CreateRouteDTO dto) {

        Route newRoute = new Route();
        newRoute.setOrigin(dto.getOrigin());
        newRoute.setDestination(dto.getDestination());
        newRoute.setDistance(dto.getDistance());
        newRoute.setEstimatedTime(dto.getEstimatedTime());
        newRoute.setTrafficLevel(dto.getTrafficLevel());

        return routeRepository.save(newRoute);
    }
}