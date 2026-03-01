package com.transporte.monitor.service;

import com.transporte.monitor.model.RegistroHorario;
import com.transporte.monitor.model.RegistroUbicacion;
import com.transporte.monitor.model.ResumenDiario;
import com.transporte.monitor.repository.RegistroHorarioRepository;
import com.transporte.monitor.repository.RegistroUbicacionRepository;
import com.transporte.monitor.repository.ResumenDiarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitorService {

    private static final Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private RegistroUbicacionRepository ubicacionRepository;

    @Autowired
    private RegistroHorarioRepository horarioRepository;

    @Autowired
    private ResumenDiarioRepository resumenRepository;

    // ── Consultas de ubicaciones ───────────────────────────

    public List<RegistroUbicacion> obtenerTodasUbicaciones() {
        return ubicacionRepository.findAll();
    }

    public List<RegistroUbicacion> obtenerUbicacionesPorVehiculo(String vehiculoId) {
        return ubicacionRepository.findByVehiculoId(vehiculoId);
    }

    public List<RegistroUbicacion> obtenerUbicacionesPorLinea(String linea) {
        return ubicacionRepository.findByLinea(linea);
    }

    public List<RegistroUbicacion> obtenerUbicacionesPorParadero(String paradero) {
        return ubicacionRepository.findByParadero(paradero);
    }

    // ── Consultas de horarios ──────────────────────────────

    public List<RegistroHorario> obtenerTodosHorarios() {
        return horarioRepository.findAll();
    }

    public List<RegistroHorario> obtenerHorariosPorVehiculo(String vehiculoId) {
        return horarioRepository.findByVehiculoId(vehiculoId);
    }

    public List<RegistroHorario> obtenerHorariosPorEstado(String estado) {
        return horarioRepository.findByEstado(estado);
    }

    public List<RegistroHorario> obtenerHorariosPorLinea(String linea) {
        return horarioRepository.findByLinea(linea);
    }

    // ── Consultas de resumen ───────────────────────────────

    public List<ResumenDiario> obtenerResumenHoy() {
        return resumenRepository.findByFecha(LocalDate.now());
    }

    public List<ResumenDiario> obtenerResumenPorFecha(LocalDate fecha) {
        return resumenRepository.findByFecha(fecha);
    }

    public List<ResumenDiario> obtenerResumenPorVehiculo(String vehiculoId) {
        return resumenRepository.findByVehiculoId(vehiculoId);
    }

    // ── Generación de resumen diario ──────────────────────
    // Se ejecuta cada hora y también al final del día a medianoche
    @Scheduled(cron = "0 0 * * * *") // cada hora
    public void generarResumenDiario() {
        log.info("Generando resumen diario...");
        LocalDate hoy = LocalDate.now();

        List<RegistroHorario> horarios = horarioRepository.findAll();
        if (horarios.isEmpty()) {
            log.info("Sin datos para generar resumen.");
            return;
        }

        // Agrupar por vehiculoId + paradero
        Map<String, List<RegistroHorario>> agrupados = horarios.stream()
                .collect(Collectors.groupingBy(h -> h.getVehiculoId() + "|" + h.getParadero()));

        for (Map.Entry<String, List<RegistroHorario>> entry : agrupados.entrySet()) {
            List<RegistroHorario> grupo = entry.getValue();
            RegistroHorario primero = grupo.get(0);

            // Calcular demora promedio
            long demoraPromedio = (long) grupo.stream()
                    .mapToLong(h -> h.getDemoraMinutos() != null ? h.getDemoraMinutos() : 0)
                    .average()
                    .orElse(0);

            // Estado más frecuente
            String estadoFrecuente = grupo.stream()
                    .collect(Collectors.groupingBy(RegistroHorario::getEstado, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("EN_HORARIO");

            LocalDateTime primeraLlegada = grupo.stream()
                    .map(RegistroHorario::getHoraLlegada)
                    .min(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.now());

            LocalDateTime ultimaLlegada = grupo.stream()
                    .map(RegistroHorario::getHoraLlegada)
                    .max(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.now());

            // Buscar si ya existe resumen para hoy, vehiculo y paradero
            List<ResumenDiario> existentes = resumenRepository
                    .findByFechaAndVehiculoIdAndParadero(hoy, primero.getVehiculoId(), primero.getParadero());

            ResumenDiario resumen = existentes.isEmpty() ? new ResumenDiario() : existentes.get(0);
            resumen.setFecha(hoy);
            resumen.setVehiculoId(primero.getVehiculoId());
            resumen.setLinea(primero.getLinea());
            resumen.setParadero(primero.getParadero());
            resumen.setPrimeraLlegada(primeraLlegada);
            resumen.setUltimaLlegada(ultimaLlegada);
            resumen.setTotalVisitas(grupo.size());
            resumen.setDemoraPromedioMinutos(demoraPromedio);
            resumen.setEstadoMasFrecuente(estadoFrecuente);

            resumenRepository.save(resumen);
        }

        log.info("Resumen diario generado: {} registros.", agrupados.size());
    }

    // Método para generar resumen manualmente desde el controller
    public void generarResumenManual() {
        generarResumenDiario();
    }
}
