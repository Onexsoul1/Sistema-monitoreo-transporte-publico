package com.transporte.procesador.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transporte.procesador.model.Horario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class HorarioProducer {

    private static final Logger log = LoggerFactory.getLogger(HorarioProducer.class);
    private static final String TOPIC = "horarios";

    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public HorarioProducer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void enviarHorario(Horario horario) {
        try {
            String mensaje = objectMapper.writeValueAsString(horario);
            kafkaTemplate.send(TOPIC, horario.getVehiculoId(), mensaje);
            log.info("Horario publicado en topic [{}]: vehiculo={} estado={} paradero={}",
                    TOPIC, horario.getVehiculoId(), horario.getEstado(), horario.getParadero());
        } catch (Exception e) {
            log.error("Error al publicar horario en Kafka: {}", e.getMessage());
        }
    }
}
