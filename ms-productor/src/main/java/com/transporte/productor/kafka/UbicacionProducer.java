package com.transporte.productor.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transporte.productor.model.Ubicacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UbicacionProducer {

    private static final Logger log = LoggerFactory.getLogger(UbicacionProducer.class);
    private static final String TOPIC = "ubicaciones_vehiculos";

    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public UbicacionProducer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void enviarUbicacion(Ubicacion ubicacion) {
        try {
            String mensaje = objectMapper.writeValueAsString(ubicacion);
            kafkaTemplate.send(TOPIC, ubicacion.getVehiculoId(), mensaje);
            log.info("Ubicacion enviada al topic [{}]: vehiculo={} paradero={}",
                    TOPIC, ubicacion.getVehiculoId(), ubicacion.getParadero());
        } catch (Exception e) {
            log.error("Error al enviar ubicacion a Kafka: {}", e.getMessage());
        }
    }
}
