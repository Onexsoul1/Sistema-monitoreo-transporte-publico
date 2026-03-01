package com.transporte.monitor.controller;

import com.transporte.monitor.model.RegistroHorario;
import com.transporte.monitor.model.RegistroUbicacion;
import com.transporte.monitor.model.ResumenDiario;
import com.transporte.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    // ── UBICACIONES ────────────────────────────────────────

    // GET - Todas las ubicaciones registradas
    @GetMapping("/ubicaciones")
    public ResponseEntity<List<RegistroUbicacion>> obtenerUbicaciones() {
        return ResponseEntity.ok(monitorService.obtenerTodasUbicaciones());
    }

    // GET - Ubicaciones por vehículo
    @GetMapping("/ubicaciones/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroUbicacion>> ubicacionesPorVehiculo(@PathVariable String vehiculoId) {
        return ResponseEntity.ok(monitorService.obtenerUbicacionesPorVehiculo(vehiculoId));
    }

    // GET - Ubicaciones por línea
    @GetMapping("/ubicaciones/linea/{linea}")
    public ResponseEntity<List<RegistroUbicacion>> ubicacionesPorLinea(@PathVariable String linea) {
        return ResponseEntity.ok(monitorService.obtenerUbicacionesPorLinea(linea));
    }

    // GET - Ubicaciones por paradero
    @GetMapping("/ubicaciones/paradero/{paradero}")
    public ResponseEntity<List<RegistroUbicacion>> ubicacionesPorParadero(@PathVariable String paradero) {
        return ResponseEntity.ok(monitorService.obtenerUbicacionesPorParadero(paradero));
    }

    // ── HORARIOS ───────────────────────────────────────────

    // GET - Todos los horarios registrados
    @GetMapping("/horarios")
    public ResponseEntity<List<RegistroHorario>> obtenerHorarios() {
        return ResponseEntity.ok(monitorService.obtenerTodosHorarios());
    }

    // GET - Horarios por vehículo
    @GetMapping("/horarios/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RegistroHorario>> horariosPorVehiculo(@PathVariable String vehiculoId) {
        return ResponseEntity.ok(monitorService.obtenerHorariosPorVehiculo(vehiculoId));
    }

    // GET - Horarios por estado (EN_HORARIO, DEMORADO, ADELANTADO)
    @GetMapping("/horarios/estado/{estado}")
    public ResponseEntity<List<RegistroHorario>> horariosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(monitorService.obtenerHorariosPorEstado(estado));
    }

    // GET - Horarios por línea
    @GetMapping("/horarios/linea/{linea}")
    public ResponseEntity<List<RegistroHorario>> horariosPorLinea(@PathVariable String linea) {
        return ResponseEntity.ok(monitorService.obtenerHorariosPorLinea(linea));
    }

    // ── RESUMEN DIARIO ─────────────────────────────────────

    // GET - Resumen de hoy
    @GetMapping("/resumen")
    public ResponseEntity<List<ResumenDiario>> resumenHoy() {
        return ResponseEntity.ok(monitorService.obtenerResumenHoy());
    }

    // GET - Resumen por fecha específica
    @GetMapping("/resumen/fecha/{fecha}")
    public ResponseEntity<List<ResumenDiario>> resumenPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(monitorService.obtenerResumenPorFecha(fecha));
    }

    // GET - Resumen por vehículo
    @GetMapping("/resumen/vehiculo/{vehiculoId}")
    public ResponseEntity<List<ResumenDiario>> resumenPorVehiculo(@PathVariable String vehiculoId) {
        return ResponseEntity.ok(monitorService.obtenerResumenPorVehiculo(vehiculoId));
    }

    // POST - Forzar generación de resumen manualmente (útil para el video)
    @PostMapping("/resumen/generar")
    public ResponseEntity<String> generarResumen() {
        monitorService.generarResumenManual();
        return ResponseEntity.ok("Resumen diario generado correctamente");
    }

    // DELETE - Limpiar registros de ubicaciones (para pruebas)
    @DeleteMapping("/ubicaciones")
    public ResponseEntity<String> limpiarUbicaciones() {
        monitorService.obtenerTodasUbicaciones(); // validar que hay datos
        return ResponseEntity.ok("Endpoint disponible - implementar si se necesita para pruebas");
    }

    // DELETE - Limpiar registros de horarios (para pruebas)
    @DeleteMapping("/horarios")
    public ResponseEntity<String> limpiarHorarios() {
        return ResponseEntity.ok("Endpoint disponible - implementar si se necesita para pruebas");
    }
}
