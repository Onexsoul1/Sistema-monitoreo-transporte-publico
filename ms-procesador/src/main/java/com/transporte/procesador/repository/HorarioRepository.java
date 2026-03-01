package com.transporte.procesador.repository;

import com.transporte.procesador.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findByVehiculoId(String vehiculoId);
    List<Horario> findByLinea(String linea);
    List<Horario> findByEstado(String estado);
    List<Horario> findByParadero(String paradero);
}
