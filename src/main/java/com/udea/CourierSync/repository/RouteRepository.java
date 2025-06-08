package com.udea.CourierSync.repository;

import com.udea.CourierSync.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByOrigin(String origin);

    List<Route> findByOriginAndDestination(String origin, String destination);

}