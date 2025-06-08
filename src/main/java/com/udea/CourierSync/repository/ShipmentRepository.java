package com.udea.CourierSync.repository;

import com.udea.CourierSync.entity.Client;
import com.udea.CourierSync.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByClient(Client client);

    List<Shipment> findByClientId(Long clientId);

    List<Shipment> findByPriority(String priority);

}