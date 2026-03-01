package com.transporte.procesador.service;

import com.transporte.procesador.model.Horario;
import com.transporte.procesador.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    private final Random random = new Random();

    // Horarios base programados por paradero (minutos desde medianoche)
    private int obtenerHorarioProgramado(String paradero) {
        return switch (paradero) {
            case "Terminal Central"    -> 480;  // 08:00
            case "Plaza de Armas"      -> 495;  // 08:15
            case "Estación Baquedano"  -> 510;  // 08:30
            case "Mall Plaza"          -> 525;  // 08:45
            case "Hospital Regional"   -> 540;  // 09:00
            case "Universidad"         -> 555;  // 09:15
            case "Mercado Central"     -> 570;  // 09:30
            case "Estadio Nacional"    -> 585;  // 09:45
            case "Aeropuerto"          -> 600;  // 10:00
            case "Puerto"              -> 615;  // 10:15
            default                    -> 480;
        };
    }

    public Horario procesarUbicacion(String vehiculoId, String linea, String paradero) {
        LocalDateTime ahora = LocalDateTime.now();

        // Calcular demora simulada (-5 a +15 minutos)
        long demoraMinutos = (random.nextInt(21) - 5);

        // Hora de llegada = ahora + demora simulada
        LocalDateTime horaLlegada = ahora.plusMinutes(demoraMinutos);
        // Hora salida = llegada + tiempo de parada (1-3 min)
        LocalDateTime horaSalida  = horaLlegada.plusMinutes(random.nextInt(3) + 1);

        // Determinar estado según demora
        String estado;
        if (demoraMinutos < -2) {
            estado = "ADELANTADO";
        } else if (demoraMinutos > 5) {
            estado = "DEMORADO";
        } else {
            estado = "EN_HORARIO";
        }

        Horario horario = new Horario();
        horario.setVehiculoId(vehiculoId);
        horario.setLinea(linea);
        horario.setParadero(paradero);
        horario.setHoraLlegada(horaLlegada);
        horario.setHoraSalida(horaSalida);
        horario.setDemoraMinutos(demoraMinutos);
        horario.setEstado(estado);

        return horarioRepository.save(horario);
    }

    public List<Horario> obtenerTodos() {
        return horarioRepository.findAll();
    }

    public Horario obtenerPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + id));
    }

    public List<Horario> obtenerPorVehiculo(String vehiculoId) {
        return horarioRepository.findByVehiculoId(vehiculoId);
    }

    public List<Horario> obtenerPorLinea(String linea) {
        return horarioRepository.findByLinea(linea);
    }

    public List<Horario> obtenerPorEstado(String estado) {
        return horarioRepository.findByEstado(estado);
    }

    public Horario actualizarHorario(Long id, Horario datos) {
        Horario existente = obtenerPorId(id);
        existente.setEstado(datos.getEstado());
        existente.setDemoraMinutos(datos.getDemoraMinutos());
        existente.setHoraLlegada(datos.getHoraLlegada());
        existente.setHoraSalida(datos.getHoraSalida());
        return horarioRepository.save(existente);
    }

    public void eliminarHorario(Long id) {
        obtenerPorId(id);
        horarioRepository.deleteById(id);
    }
}
