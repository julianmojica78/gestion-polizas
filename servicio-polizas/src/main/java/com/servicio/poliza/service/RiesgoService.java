package com.servicio.poliza.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.servicio.poliza.exception.PolizaException;
import com.servicio.poliza.model.Poliza;
import com.servicio.poliza.model.Riesgo;
import com.servicio.poliza.model.dto.EstadoPoliza;
import com.servicio.poliza.model.dto.EstadoRiesgo;
import com.servicio.poliza.model.dto.RiesgoRequest;
import com.servicio.poliza.model.dto.RiesgoResponse;
import com.servicio.poliza.model.dto.TipoPoliza;
import com.servicio.poliza.repository.PolizaRepository;
import com.servicio.poliza.repository.RiesgoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiesgoService {

	private final PolizaRepository polizaRepository;
	private final RiesgoRepository riesgoRepository;
	private final CoreService coreService;

	public RiesgoResponse agregar(Long polizaId, RiesgoRequest request) {

		Poliza poliza = polizaRepository.findById(polizaId)
				.orElseThrow(() -> new PolizaException("Póliza no encontrada", "POL-001",
						"No existe una póliza registrada con el identificador: " + polizaId,
						"/polizas/" + polizaId + "/riesgos", HttpStatus.NOT_FOUND));

		if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
			throw new PolizaException("Tipo de póliza no permitido", "POL-004",
					"Solamente se pueden agregar riesgos a pólizas colectivas.", "/polizas/" + polizaId + "/riesgos",
					HttpStatus.BAD_REQUEST);
		}

		if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
			throw new PolizaException("Operación no permitida", "POL-002",
					"No se pueden agregar riesgos a una póliza cancelada.", "/polizas/" + polizaId + "/riesgos",
					HttpStatus.BAD_REQUEST);
		}

		Riesgo riesgo = new Riesgo();

		riesgo.setPoliza(poliza);
		riesgo.setIdentificadorInmueble(request.identificadorInmueble());
		riesgo.setDireccion(request.direccion());
		riesgo.setCanonMensual(request.canonMensual());
		riesgo.setEstado(EstadoRiesgo.ACTIVO);
		riesgo.setFechaCreacion(LocalDateTime.now());

		Riesgo guardado = riesgoRepository.save(riesgo);

		coreService.notificarActualizacion(polizaId);

		return new RiesgoResponse(guardado.getId(), polizaId, guardado.getIdentificadorInmueble(),
				guardado.getDireccion(), guardado.getCanonMensual(), guardado.getEstado());
	}

	@Transactional
	public void cancelar(Long riesgoId) {

		Riesgo riesgo = riesgoRepository.findById(riesgoId)
				.orElseThrow(() -> new PolizaException("Riesgo no encontrado", "POL-005",
						"No existe un riesgo registrado con el identificador: " + riesgoId,
						"/riesgos/" + riesgoId + "/cancelar", HttpStatus.NOT_FOUND));

		if (riesgo.getEstado() == EstadoRiesgo.CANCELADO) {
			throw new PolizaException("Operación no permitida", "POL-006", "El riesgo ya se encuentra cancelado.",
					"/riesgos/" + riesgoId + "/cancelar", HttpStatus.BAD_REQUEST);
		}

		riesgo.setEstado(EstadoRiesgo.CANCELADO);
		riesgo.setFechaActualizacion(LocalDateTime.now());

		riesgoRepository.save(riesgo);

		coreService.notificarActualizacion(riesgo.getPoliza().getId());
	}
}
