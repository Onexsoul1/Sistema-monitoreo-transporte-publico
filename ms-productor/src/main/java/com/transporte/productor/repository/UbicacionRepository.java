package com.transporte.productor.repository;

import com.transporte.productor.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByVehiculoId(String vehiculoId);
    List<Ubicacion> findByLinea(String linea);
}
