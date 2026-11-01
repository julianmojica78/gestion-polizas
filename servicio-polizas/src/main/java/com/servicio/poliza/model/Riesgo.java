package com.servicio.poliza.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.servicio.poliza.model.dto.EstadoRiesgo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "riesgo")
@Getter
@Setter
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id", nullable = false)
    private Poliza poliza;

    @Column(name = "identificador_inmueble")
    private String identificadorInmueble;

    private String direccion;

    @Column(name = "canon_mensual")
    private BigDecimal canonMensual;

    @Enumerated(EnumType.STRING)
    private EstadoRiesgo estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}