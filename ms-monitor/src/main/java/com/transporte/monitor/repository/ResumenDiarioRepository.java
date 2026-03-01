package com.transporte.monitor.repository;

import com.transporte.monitor.model.ResumenDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResumenDiarioRepository extends JpaRepository<ResumenDiario, Long> {
    List<ResumenDiario> findByFecha(LocalDate fecha);
    List<ResumenDiario> findByVehiculoId(String vehiculoId);
    List<ResumenDiario> findByFechaAndVehiculoIdAndParadero(LocalDate fecha, String vehiculoId, String paradero);
}
