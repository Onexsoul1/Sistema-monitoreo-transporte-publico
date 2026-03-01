package com.transporte.productor.service;

import com.transporte.productor.model.Ubicacion;
import com.transporte.productor.repository.UbicacionRepository;
import com.transporte.productor.kafka.UbicacionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UbicacionProducer ubicacionProducer;

    public List<Ubicacion> obtenerTodas() {
        return ubicacionRepository.findAll();
    }

    public Ubicacion obtenerPorId(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + id));
    }

    public List<Ubicacion> obtenerPorVehiculo(String vehiculoId) {
        return ubicacionRepository.findByVehiculoId(vehiculoId);
    }

    public List<Ubicacion> obtenerPorLinea(String linea) {
        return ubicacionRepository.findByLinea(linea);
    }

    public Ubicacion crearUbicacion(Ubicacion ubicacion) {
        ubicacion.setTimestamp(LocalDateTime.now());
        Ubicacion guardada = ubicacionRepository.save(ubicacion);
        ubicacionProducer.enviarUbicacion(guardada);
        return guardada;
    }

    public Ubicacion actualizarUbicacion(Long id, Ubicacion datos) {
        Ubicacion existente = obtenerPorId(id);
        existente.setVehiculoId(datos.getVehiculoId());
        existente.setLinea(datos.getLinea());
        existente.setLatitud(datos.getLatitud());
        existente.setLongitud(datos.getLongitud());
        existente.setParadero(datos.getParadero());
        existente.setTimestamp(LocalDateTime.now());
        Ubicacion actualizada = ubicacionRepository.save(existente);
        ubicacionProducer.enviarUbicacion(actualizada);
        return actualizada;
    }

    public void eliminarUbicacion(Long id) {
        obtenerPorId(id); // valida que existe
        ubicacionRepository.deleteById(id);
    }
}
