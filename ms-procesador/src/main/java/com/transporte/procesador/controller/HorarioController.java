package com.transporte.procesador.controller;

import com.transporte.procesador.model.Horario;
import com.transporte.procesador.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    // GET - Obtener todos los horarios
    @GetMapping
    public ResponseEntity<List<Horario>> obtenerTodos() {
        return ResponseEntity.ok(horarioService.obtenerTodos());
    }

    // GET - Obtener horario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Horario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerPorId(id));
    }

    // GET - Obtener horarios por vehículo
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<Horario>> obtenerPorVehiculo(@PathVariable String vehiculoId) {
        return ResponseEntity.ok(horarioService.obtenerPorVehiculo(vehiculoId));
    }

    // GET - Obtener horarios por línea
    @GetMapping("/linea/{linea}")
    public ResponseEntity<List<Horario>> obtenerPorLinea(@PathVariable String linea) {
        return ResponseEntity.ok(horarioService.obtenerPorLinea(linea));
    }

    // GET - Obtener horarios por estado (EN_HORARIO, DEMORADO, ADELANTADO)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Horario>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(horarioService.obtenerPorEstado(estado));
    }

    // POST - Crear horario manualmente
    @PostMapping
    public ResponseEntity<Horario> crearHorario(@RequestBody Horario horario) {
        // Usar procesarUbicacion para respetar la lógica de negocio
        Horario creado = horarioService.procesarUbicacion(
                horario.getVehiculoId(),
                horario.getLinea(),
                horario.getParadero()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT - Actualizar horario existente
    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizarHorario(
            @PathVariable Long id,
            @RequestBody Horario horario) {
        return ResponseEntity.ok(horarioService.actualizarHorario(id, horario));
    }

    // DELETE - Eliminar horario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
