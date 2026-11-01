
INSERT INTO poliza (
    tipo,
    estado,
    canon_mensual,
    prima,
    meses_vigencia,
    ipc_aplicable,
    fecha_inicio,
    fecha_fin
) VALUES
(
    'INDIVIDUAL',
    'ACTIVA',
    1500000,
    1800000,
    12,
    0.05,
    '2026-01-01',
    '2026-12-31'
),
(
    'COLECTIVA',
    'ACTIVA',
    2000000,
    24000000,
    12,
    0.05,
    '2026-01-01',
    '2026-12-31'
),
(
    'COLECTIVA',
    'CANCELADA',
    1800000,
    21600000,
    12,
    0.05,
    '2026-01-01',
    '2026-12-31'
);

INSERT INTO riesgo (
    poliza_id,
    identificador_inmueble,
    direccion,
    canon_mensual,
    estado
) VALUES
(1, 'INM-001', 'Calle 100 # 15-20', 1500000, 'ACTIVO'),
(2, 'INM-002', 'Carrera 10 # 20-30', 1200000, 'ACTIVO'),
(2, 'INM-003', 'Calle 80 # 10-15', 800000, 'ACTIVO'),
(3, 'INM-004', 'Carrera 15 # 30-40', 1800000, 'CANCELADO');