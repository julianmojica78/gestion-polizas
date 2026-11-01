package com.servicio.poliza.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RiesgoRequest(

		@NotBlank(message = "El identificador del inmueble es obligatorio") String identificadorInmueble,

		@NotBlank(message = "La dirección es obligatoria") String direccion,

		@NotNull(message = "El canon mensual es obligatorio") @Positive(message = "El canon debe ser mayor a cero") BigDecimal canonMensual

) {
}