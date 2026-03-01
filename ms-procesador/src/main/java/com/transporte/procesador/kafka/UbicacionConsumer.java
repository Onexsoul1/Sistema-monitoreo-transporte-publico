package com.transporte.procesador.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transporte.procesador.kafka.HorarioProducer;
import com.transporte.procesador.service.HorarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UbicacionConsumer {

    private static final Logger log = LoggerFactory.getLogger(UbicacionConsumer.class);

    private final ObjectMapper objectMapper;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private HorarioProducer horarioProducer;

    public UbicacionConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "ubicaciones_vehiculos", groupId = "procesador-group")
    public void consumirUbicacion(String mensaje) {
        try {
            // Deserializar el mensaje JSON
            Map<String, Object> ubicacion = objectMapper.readValue(mensaje, Map.class);

            String vehiculoId = (String) ubicacion.get("vehiculoId");
            String linea      = (String) ubicacion.get("linea");
            String paradero   = (String) ubicacion.get("paradero");

            log.info("Ubicacion recibida: vehiculo={} paradero={}", vehiculoId, paradero);

            // Procesar y generar horario actualizado
            var horario = horarioService.procesarUbicacion(vehiculoId, linea, paradero);

            // Publicar el horario generado en el topic horarios
            horarioProducer.enviarHorario(horario);

        } catch (Exception e) {
            log.error("Error procesando ubicacion: {}", e.getMessage());
        }
    }
}
