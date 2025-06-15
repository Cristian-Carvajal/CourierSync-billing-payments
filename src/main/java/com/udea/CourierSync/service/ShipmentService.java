package com.udea.CourierSync.service;

import com.udea.CourierSync.dto.CreateShipmentDTO;
import com.udea.CourierSync.entity.Client;
import com.udea.CourierSync.entity.Route;
import com.udea.CourierSync.entity.Shipment;
import com.udea.CourierSync.exception.ResourceNotFoundException;
import com.udea.CourierSync.repository.ClientRepository;
import com.udea.CourierSync.repository.RouteRepository;
import com.udea.CourierSync.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ClientRepository clientRepository;
    private final RouteRepository routeRepository;
    private final InvoiceService invoiceService;

    @Transactional
    public Shipment createShipmentAndInvoice(CreateShipmentDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id no encontrado: " + dto.getClientId()));

        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta con id no encontrada: " + dto.getRouteId()));

        Shipment newShipment = new Shipment();
        newShipment.setClient(client);
        newShipment.setRoute(route);
        newShipment.setWeight(dto.getWeight());
        newShipment.setPriority(dto.getPriority());

        Shipment savedShipment = shipmentRepository.save(newShipment);

        invoiceService.createInvoiceForShipment(savedShipment);

        return savedShipment;
    }

    public Shipment findById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío con id " + id + " no encontrado"));
    }
}