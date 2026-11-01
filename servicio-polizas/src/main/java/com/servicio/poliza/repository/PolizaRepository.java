package com.servicio.poliza.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicio.poliza.model.Poliza;
import com.servicio.poliza.model.dto.EstadoPoliza;
import com.servicio.poliza.model.dto.TipoPoliza;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {

	Page<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado, Pageable pageable);

	Page<Poliza> findByTipo(TipoPoliza tipo, Pageable pageable);

	Page<Poliza> findByEstado(EstadoPoliza estado, Pageable pageable);
}
