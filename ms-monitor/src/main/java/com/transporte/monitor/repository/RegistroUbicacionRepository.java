package com.transporte.monitor.repository;

import com.transporte.monitor.model.RegistroUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroUbicacionRepository extends JpaRepository<RegistroUbicacion, Long> {
    List<RegistroUbicacion> findByVehiculoId(String vehiculoId);
    List<RegistroUbicacion> findByLinea(String linea);
    List<RegistroUbicacion> findByParadero(String paradero);
    List<RegistroUbicacion> findByTimestampBetween(LocalDateTime desde, LocalDateTime hasta);
}
