package com.servicio.poliza.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.poliza.model.dto.RiesgoRequest;
import com.servicio.poliza.model.dto.RiesgoResponse;
import com.servicio.poliza.service.RiesgoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService riesgoService;

    @PostMapping("/polizas/{id}/riesgos")
    public ResponseEntity<RiesgoResponse> agregar(

            @PathVariable Long id,

            @Valid @RequestBody RiesgoRequest request) {

        RiesgoResponse response =
                riesgoService.agregar(id, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/riesgos/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id) {

        riesgoService.cancelar(id);

        return ResponseEntity.noContent().build();
    }
}
