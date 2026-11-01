# Gestión de Pólizas

## 1. Descripción

Microservicio desarrollado en Java 17 y Spring Boot para la gestión de pólizas individuales y colectivas, permitiendo consultar, renovar y cancelar pólizas, así como administrar los riesgos asociados.

El servicio permite:

- Consultar pólizas aplicando filtros por tipo y estado.
- Consultar los riesgos asociados a una póliza.
- Renovar pólizas aplicando el porcentaje de IPC configurado.
- Cancelar pólizas y los riesgos asociados.
- Agregar riesgos únicamente a pólizas colectivas.
- Cancelar riesgos individualmente.
- Simular la notificación de eventos hacia un servicio CORE.

## 2. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 17 | Lenguaje de desarrollo |
| Spring Boot | Framework principal |
| Spring Web MVC | Exposición de servicios REST |
| Spring Data JPA | Persistencia de datos |
| PostgreSQL | Base de datos |
| Maven | Gestión de dependencias y compilación |
| Bean Validation | Validación de datos de entrada |

## 3. Requisitos previos

Para ejecutar el proyecto se requiere:

- JDK 17.
- Maven 3.8 o superior.
- PostgreSQL.
- IDE compatible con proyectos Maven (IntelliJ IDEA, Spring Tool Suite o Eclipse).

## 4. Configuración de la base de datos

Crear la base de datos:

```sql
CREATE DATABASE gestion_polizas;
```

Ejecutar los scripts SQL en el siguiente orden:

1. `src/main/resources/db/01-schema.sql`
2. `src/main/resources/db/02-data.sql`

El primer archivo crea las tablas necesarias y el segundo registra datos iniciales para realizar pruebas.

### Tablas utilizadas

**POLIZA**

Almacena la información principal de las pólizas:

- Identificador.
- Tipo de póliza: INDIVIDUAL o COLECTIVA.
- Estado: ACTIVA, RENOVADA o CANCELADA.
- Canon mensual.
- Prima.
- Meses de vigencia.
- Porcentaje de IPC aplicable.
- Fecha de inicio y fecha de finalización.
- Fechas de creación y actualización.

**RIESGO**

Almacena los riesgos asociados a cada póliza:

- Identificador.
- Identificador de la póliza.
- Identificador del inmueble.
- Dirección.
- Canon mensual.
- Estado: ACTIVO o CANCELADO.
- Fechas de creación y actualización.

## 5. Configuración del proyecto

Modificar las propiedades de conexión a la base de datos en `application.yml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: gestion-polizas

  datasource:
    url: jdbc:postgresql://localhost:5432/gestion_polizas
    username: postgres
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true

integracion:
  core:
    url: ${CORE_URL:http://localhost:8080}

security:
  api-key: ${API_KEY:123456}
```

### Variables de entorno

| Variable | Descripción | Valor por defecto |
|---|---|---|
| DB_PASSWORD | Contraseña de PostgreSQL | postgres |
| CORE_URL | URL del servicio CORE | http://localhost:8080 |
| API_KEY | Clave de autenticación | 123456 |

## 6. Seguridad

Todos los endpoints requieren el encabezado:

```http
x-api-key: 123456
```

La clave se configura mediante la variable de entorno `API_KEY`.

Si la clave no se envía o no coincide con la configurada, el servicio responde con HTTP 401 (Unauthorized).

## 7. Endpoints disponibles

La URL base del servicio es:

```text
http://localhost:8080
```

### 7.1. Consultar pólizas

**GET** `/polizas`

Permite consultar las pólizas registradas aplicando filtros opcionales.

Parámetros:

| Parámetro | Descripción | Valores |
|---|---|---|
| tipo | Tipo de póliza | INDIVIDUAL, COLECTIVA |
| estado | Estado de la póliza | ACTIVA, RENOVADA, CANCELADA |
| page | Número de página | 0 por defecto |
| size | Cantidad de registros | 20 por defecto |

Ejemplo:

```http
GET /polizas?tipo=COLECTIVA&estado=ACTIVA&page=0&size=10
```

### 7.2. Consultar riesgos de una póliza

**GET** `/polizas/{id}/riesgos`

Permite consultar los riesgos asociados a una póliza.

Ejemplo:

```http
GET /polizas/2/riesgos
```

### 7.3. Renovar póliza

**POST** `/polizas/{id}/renovar`

Renueva una póliza aplicando el IPC configurado.

La operación actualiza el canon mensual, calcula la nueva prima y modifica