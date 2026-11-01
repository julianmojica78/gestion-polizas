package com.servicio.poliza.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.poliza.model.dto.CoreEventoRequest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/core-mock")
@Slf4j
public class CoreMockController {

	@PostMapping("/evento")
	public ResponseEntity<Void> recibirEvento(@Valid @RequestBody CoreEventoRequest request) {

		log.info("Intento de envío al CORE. Evento: {}, PolizaId: {}", request.evento(), request.polizaId());

		return ResponseEntity.accepted().build();
	}
}