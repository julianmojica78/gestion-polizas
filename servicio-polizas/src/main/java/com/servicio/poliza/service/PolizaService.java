package com.servicio.poliza.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.servicio.poliza.exception.PolizaException;
import com.servicio.poliza.model.Poliza;
import com.servicio.poliza.model.Riesgo;
import com.servicio.poliza.model.dto.EstadoPoliza;
import com.servicio.poliza.model.dto.EstadoRiesgo;
import com.servicio.poliza.model.dto.PolizaResponse;
import com.servicio.poliza.model.dto.RiesgoResponse;
import com.servicio.poliza.model.dto.TipoPoliza;
import com.servicio.poliza.repository.PolizaRepository;
import com.servicio.poliza.repository.RiesgoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolizaService {

	private final PolizaRepository polizaRepository;
	private final RiesgoRepository riesgoRepository;
	private final CoreService coreService;

	public Page<PolizaResponse> listar(TipoPoliza tipo, EstadoPoliza estado, Pageable pageable) {

		Page<Poliza> polizas;

		if (tipo != null && estado != null) {
			polizas = polizaRepository.findByTipoAndEstado(tipo, estado, pageable);

		} else if (tipo != null) {
			polizas = polizaRepository.findByTipo(tipo, pageable);

		} else if (estado != null) {
			polizas = polizaRepository.findByEstado(estado, pageable);

		} else {
			polizas = polizaRepository.findAll(pageable);
		}

		return polizas.map(this::toResponse);
	}

	public List<RiesgoResponse> consultarRiesgos(Long polizaId) {

		validarExistencia(polizaId);

		return riesgoRepository.findByPolizaId(polizaId).stream().map(this::toRiesgoResponse).toList();
	}

	public PolizaResponse renovar(Long id) {

		Poliza poliza = obtenerPoliza(id);

		if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
			throw new PolizaException("Operación no permitida", "POL-002", "No se puede renovar una póliza cancelada.",
					"/polizas/" + id + "/renovar", HttpStatus.BAD_REQUEST);
		}

		BigDecimal factor = BigDecimal.ONE.add(poliza.getIpcAplicable());

		BigDecimal nuevoCanon = poliza.getCanonMensual().multiply(factor).setScale(2, RoundingMode.HALF_UP);

		BigDecimal nuevaPrima = nuevoCanon.multiply(BigDecimal.valueOf(poliza.getMesesVigencia())).setScale(2,
				RoundingMode.HALF_UP);

		poliza.setCanonMensual(nuevoCanon);
		poliza.setPrima(nuevaPrima);
		poliza.setEstado(EstadoPoliza.RENOVADA);
		poliza.setFechaActualizacion(LocalDateTime.now());

		coreService.notificarActualizacion(id);

		return toResponse(polizaRepository.save(poliza));
	}

	@Transactional
	public void cancelar(Long id) {

		Poliza poliza = obtenerPoliza(id);

		if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
			throw new PolizaException("Operación no permitida", "POL-003", "La póliza ya se encuentra cancelada.",
					"/polizas/" + id + "/cancelar", HttpStatus.BAD_REQUEST);
		}

		poliza.setEstado(EstadoPoliza.CANCELADA);
		poliza.setFechaActualizacion(LocalDateTime.now());

		List<Riesgo> riesgos = riesgoRepository.findByPolizaId(id);

		riesgos.forEach(riesgo -> {
			riesgo.setEstado(EstadoRiesgo.CANCELADO);
			riesgo.setFechaActualizacion(LocalDateTime.now());
		});

		coreService.notificarActualizacion(id);

		polizaRepository.save(poliza);
		riesgoRepository.saveAll(riesgos);
	}

	private Poliza obtenerPoliza(Long id) {

		return polizaRepository.findById(id).orElseThrow(() -> new PolizaException("Póliza no encontrada", "POL-001",
				"No existe una póliza registrada con el identificador: " + id, "/polizas/" + id, HttpStatus.NOT_FOUND));
	}

	private void validarExistencia(Long id) {
		obtenerPoliza(id);
	}

	private PolizaResponse toResponse(Poliza poliza) {

		return new PolizaResponse(poliza.getId(), poliza.getTipo(), poliza.getEstado(), poliza.getCanonMensual(),
				poliza.getPrima(), poliza.getMesesVigencia(), poliza.getFechaInicio(), poliza.getFechaFin());
	}

	private RiesgoResponse toRiesgoResponse(Riesgo riesgo) {

		return new RiesgoResponse(riesgo.getId(), riesgo.getPoliza().getId(), riesgo.getIdentificadorInmueble(),
				riesgo.getDireccion(), riesgo.getCanonMensual(), riesgo.getEstado());
	}
}