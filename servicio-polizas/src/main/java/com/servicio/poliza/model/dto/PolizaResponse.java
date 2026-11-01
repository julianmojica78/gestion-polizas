package com.servicio.poliza.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolizaResponse(Long id, TipoPoliza tipo, EstadoPoliza estado, BigDecimal canonMensual, BigDecimal prima,
		Integer mesesVigencia, LocalDate fechaInicio, LocalDate fechaFin) {
}