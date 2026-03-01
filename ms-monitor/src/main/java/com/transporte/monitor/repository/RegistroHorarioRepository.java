package com.transporte.monitor.repository;

import com.transporte.monitor.model.RegistroHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroHorarioRepository extends JpaRepository<RegistroHorario, Long> {
    List<RegistroHorario> findByVehiculoId(String vehiculoId);
    List<RegistroHorario> findByLinea(String linea);
    List<RegistroHorario> findByEstado(String estado);
    List<RegistroHorario> findByParadero(String paradero);
}
