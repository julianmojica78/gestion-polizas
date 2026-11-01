package com.servicio.poliza.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRespuestaExcepcionDTO {    

    private String tipoError;
    private String tituloError;
    private String codigoError;
    private String detalleError;
    private String instanciaError;

    public ApiRespuestaExcepcionDTO(String tipoError, String tituloError, String codigoError, String instanciaError) {
        this.tipoError = tipoError;
        this.tituloError = tituloError;
        this.codigoError = codigoError;
        this.instanciaError = instanciaError;
    }
}