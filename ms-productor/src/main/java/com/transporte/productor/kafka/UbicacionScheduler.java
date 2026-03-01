package com.transporte.productor.kafka;

import com.transporte.productor.model.Ubicacion;
import com.transporte.productor.repository.UbicacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class UbicacionScheduler {

    private static final Logger log = LoggerFactory.getLogger(UbicacionScheduler.class);

    // Datos simulados de la red de transporte
    private static final String[][] VEHICULOS = {
        {"V001", "Línea 1"}, {"V002", "Línea 1"}, {"V003", "Línea 2"},
        {"V004", "Línea 2"}, {"V005", "Línea 3"}, {"V006", "Línea 3"}
    };

    private static final String[][] PARADEROS = {
        {"Terminal Central",    "-33.4500", "-70.6700"},
        {"Plaza de Armas",      "-33.4372", "-70.6506"},
        {"Estación Baquedano",  "-33.4383", "-70.6340"},
        {"Mall Plaza",          "-33.4200", "-70.6100"},
        {"Hospital Regional",   "-33.4550", "-70.6800"},
        {"Universidad",         "-33.4600", "-70.6600"},
        {"Mercado Central",     "-33.4390", "-70.6490"},
        {"Estadio Nacional",    "-33.4650", "-70.6080"},
        {"Aeropuerto",          "-33.3930", "-70.7850"},
        {"Puerto",              "-33.4750", "-70.6200"}
    };

    private final Random random = new Random();

    @Autowired
    private UbicacionProducer ubicacionProducer;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    // Se ejecuta cada 1 segundo
    @Scheduled(fixedRate = 1000)
    public void generarUbicacion() {
        // Seleccionar vehículo y paradero aleatorio
        String[] vehiculo  = VEHICULOS[random.nextInt(VEHICULOS.length)];
        String[] paradero  = PARADEROS[random.nextInt(PARADEROS.length)];

        // Agregar pequeña variación aleatoria a las coordenadas (simula movimiento)
        double latVariacion = (random.nextDouble() - 0.5) * 0.005;
        double lonVariacion = (random.nextDouble() - 0.5) * 0.005;

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setVehiculoId(vehiculo[0]);
        ubicacion.setLinea(vehiculo[1]);
        ubicacion.setLatitud(Double.parseDouble(paradero[1]) + latVariacion);
        ubicacion.setLongitud(Double.parseDouble(paradero[2]) + lonVariacion);
        ubicacion.setParadero(paradero[0]);
        ubicacion.setTimestamp(LocalDateTime.now());

        // Guardar en H2 y publicar en Kafka
        ubicacionRepository.save(ubicacion);
        ubicacionProducer.enviarUbicacion(ubicacion);
    }
}
