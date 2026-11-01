package com.servicio.poliza.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.servicio.poliza.model.dto.EstadoPoliza;
import com.servicio.poliza.model.dto.TipoPoliza;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "poliza")
@Getter
@Setter
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPoliza tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPoliza estado;

    @Column(name = "canon_mensual", nullable = false)
    private BigDecimal canonMensual;

    @Column(nullable = false)
    private BigDecimal prima;

    @Column(name = "meses_vigencia", nullable = false)
    private Integer mesesVigencia;

    @Column(name = "ipc_aplicable", nullable = false)
    private BigDecimal ipcAplicable;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "poliza", fetch = FetchType.LAZY)
    private List<Riesgo> riesgos = new ArrayList<>();
}
