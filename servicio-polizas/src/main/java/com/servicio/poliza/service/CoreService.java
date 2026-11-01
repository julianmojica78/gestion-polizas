package com.servicio.poliza.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.servicio.poliza.exception.PolizaException;
import com.servicio.poliza.model.dto.CoreEventoRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoreService {

	private final RestClient coreRestClient;

	public void notificarActualizacion(Long polizaId) {

		CoreEventoRequest request = new CoreEventoRequest("ACTUALIZACION", polizaId);

		try {

			coreRestClient.post().uri("/core-mock/evento").body(request).retrieve().toBodilessEntity();

		} catch (RestClientException exception) {

			throw new PolizaException("Error de integración con CORE", "POL-007",
					"No fue posible enviar la actualización de la póliza al servicio CORE.", "/polizas/" + polizaId,
					HttpStatus.BAD_GATEWAY);
		}
	}
}