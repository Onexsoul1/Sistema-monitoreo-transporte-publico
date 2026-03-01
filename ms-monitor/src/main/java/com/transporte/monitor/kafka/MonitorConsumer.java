package com.transporte.monitor.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transporte.monitor.model.RegistroHorario;
import com.transporte.monitor.model.RegistroUbicacion;
import com.transporte.monitor.repository.RegistroHorarioRepository;
import com.transporte.monitor.repository.RegistroUbicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MonitorConsumer {

    private static final Logger log = LoggerFactory.getLogger(MonitorConsumer.class);

    private final ObjectMapper objectMapper;

    @Autowired
    private RegistroUbicacionRepository ubicacionRepository;

    @Autowired
    private RegistroHorarioRepository horarioRepository;

    public MonitorConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // Escucha el topic de ubicaciones
    @KafkaListener(topics = "ubicaciones_vehiculos", groupId = "monitor-ubicaciones-group")
    public void consumirUbicacion(String mensaje) {
        try {
            Map<String, Object> data = objectMapper.readValue(mensaje, Map.class);

            RegistroUbicacion registro = new RegistroUbicacion();
            registro.setVehiculoId((String) data.get("vehiculoId"));
            registro.setLinea((String) data.get("linea"));
            registro.setParadero((String) data.get("paradero"));
            registro.setLatitud(((Number) data.get("latitud")).doubleValue());
            registro.setLongitud(((Number) data.get("longitud")).doubleValue());
            registro.setTimestamp(LocalDateTime.now());

            ubicacionRepository.save(registro);
            log.info("Ubicacion registrada: vehiculo={} paradero={}", registro.getVehiculoId(), registro.getParadero());

        } catch (Exception e) {
            log.error("Error al consumir ubicacion en monitor: {}", e.getMessage());
        }
    }

    // Escucha el topic de horarios
    @KafkaListener(topics = "horarios", groupId = "monitor-horarios-group")
    public void consumirHorario(String mensaje) {
        try {
            Map<String, Object> data = objectMapper.readValue(mensaje, Map.class);

            RegistroHorario registro = new RegistroHorario();
            registro.setVehiculoId((String) data.get("vehiculoId"));
            registro.setLinea((String) data.get("linea"));
            registro.setParadero((String) data.get("paradero"));
            registro.setDemoraMinutos(((Number) data.get("demoraMinutos")).longValue());
            registro.setEstado((String) data.get("estado"));
            registro.setHoraLlegada(LocalDateTime.now());
            registro.setHoraSalida(LocalDateTime.now().plusMinutes(2));
            registro.setRegistradoEn(LocalDateTime.now());

            horarioRepository.save(registro);
            log.info("Horario registrado: vehiculo={} estado={} paradero={}", registro.getVehiculoId(), registro.getEstado(), registro.getParadero());

        } catch (Exception e) {
            log.error("Error al consumir horario en monitor: {}", e.getMessage());
        }
    }
}
