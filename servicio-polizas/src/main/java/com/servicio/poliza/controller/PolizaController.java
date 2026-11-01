package com.servicio.poliza.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.poliza.model.dto.EstadoPoliza;
import com.servicio.poliza.model.dto.PolizaResponse;
import com.servicio.poliza.model.dto.RiesgoResponse;
import com.servicio.poliza.model.dto.TipoPoliza;
import com.servicio.poliza.service.PolizaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
public class PolizaController {

	private final PolizaService polizaService;

	@GetMapping
	public ResponseEntity<Page<PolizaResponse>> listar(

			@RequestParam(required = false) TipoPoliza tipo,

			@RequestParam(required = false) EstadoPoliza estado,

			@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

		return ResponseEntity.ok(polizaService.listar(tipo, estado, pageable));
	}

	@GetMapping("/{id}/riesgos")
	public ResponseEntity<List<RiesgoResponse>> consultarRiesgos(@PathVariable Long id) {

		return ResponseEntity.ok(polizaService.consultarRiesgos(id));
	}

	@PostMapping("/{id}/renovar")
	public ResponseEntity<PolizaResponse> renovar(@PathVariable Long id) {

		return ResponseEntity.ok(polizaService.renovar(id));
	}

	@PostMapping("/{id}/cancelar")
	public ResponseEntity<Void> cancelar(@PathVariable Long id) {

		polizaService.cancelar(id);

		return ResponseEntity.noContent().build();
	}
}