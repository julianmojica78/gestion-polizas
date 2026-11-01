package com.servicio.poliza.model.dto;

import java.math.BigDecimal;

public record RiesgoResponse(Long id, Long polizaId, String identificadorInmueble, String direccion,
		BigDecimal canonMensual, EstadoRiesgo estado) {
}