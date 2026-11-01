CREATE TABLE poliza (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    canon_mensual NUMERIC(15,2) NOT NULL,
    prima NUMERIC(15,2) NOT NULL,
    meses_vigencia INTEGER NOT NULL,
    ipc_aplicable NUMERIC(8,5) NOT NULL DEFAULT 0,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,

    CONSTRAINT chk_poliza_tipo
        CHECK (tipo IN ('INDIVIDUAL', 'COLECTIVA')),

    CONSTRAINT chk_poliza_estado
        CHECK (estado IN ('ACTIVA', 'RENOVADA', 'CANCELADA')),

    CONSTRAINT chk_poliza_canon
        CHECK (canon_mensual > 0),

    CONSTRAINT chk_poliza_prima
        CHECK (prima >= 0),

    CONSTRAINT chk_poliza_vigencia
        CHECK (meses_vigencia > 0),

    CONSTRAINT chk_poliza_fechas
        CHECK (fecha_fin > fecha_inicio)
        
        
CREATE TABLE riesgo (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    poliza_id BIGINT NOT NULL,
    identificador_inmueble VARCHAR(100) NOT NULL,
    direccion VARCHAR(250) NOT NULL,
    canon_mensual NUMERIC(15,2) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,

    CONSTRAINT fk_riesgo_poliza
        FOREIGN KEY (poliza_id)
        REFERENCES poliza(id),

    CONSTRAINT chk_riesgo_estado
        CHECK (estado IN ('ACTIVO', 'CANCELADO')),

    CONSTRAINT chk_riesgo_canon
        CHECK (canon_mensual > 0)
);
);
