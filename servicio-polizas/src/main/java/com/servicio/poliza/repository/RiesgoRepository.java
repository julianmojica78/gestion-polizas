package com.servicio.poliza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicio.poliza.model.Riesgo;
import com.servicio.poliza.model.dto.EstadoRiesgo;

@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {

	List<Riesgo> findByPolizaId(Long polizaId);

	long countByPolizaIdAndEstado(Long polizaId, EstadoRiesgo estado);
}
