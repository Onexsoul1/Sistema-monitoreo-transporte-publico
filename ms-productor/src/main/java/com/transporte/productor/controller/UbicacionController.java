package com.transporte.productor.controller;

import com.transporte.productor.model.Ubicacion;
import com.transporte.productor.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    // GET - Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<List<Ubicacion>> obtenerTodas() {
        return ResponseEntity.ok(ubicacionService.obtenerTodas());
    }

    // GET - Obtener ubicación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ubicacionService.obtenerPorId(id));
    }

    // GET - Obtener ubicaciones por vehículo
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<Ubicacion>> obtenerPorVehiculo(@PathVariable String vehiculoId) {
        return ResponseEntity.ok(ubicacionService.obtenerPorVehiculo(vehiculoId));
    }

    // GET - Obtener ubicaciones por línea
    @GetMapping("/linea/{linea}")
    public ResponseEntity<List<Ubicacion>> obtenerPorLinea(@PathVariable String linea) {
        return ResponseEntity.ok(ubicacionService.obtenerPorLinea(linea));
    }

    // POST - Crear ubicación manualmente (también la publica en Kafka)
    @PostMapping
    public ResponseEntity<Ubicacion> crearUbicacion(@RequestBody Ubicacion ubicacion) {
        Ubicacion creada = ubicacionService.crearUbicacion(ubicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT - Actualizar ubicación existente
    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> actualizarUbicacion(
            @PathVariable Long id,
            @RequestBody Ubicacion ubicacion) {
        return ResponseEntity.ok(ubicacionService.actualizarUbicacion(id, ubicacion));
    }

    // DELETE - Eliminar ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUbicacion(@PathVariable Long id) {
        ubicacionService.eliminarUbicacion(id);
        return ResponseEntity.noContent().build();
    }
}
