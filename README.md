# PF-12 — Agenda de Clínica Dental

## Portada

- **Universidad:** Universidad Mariano Gálvez de Guatemala
- **Carrera:** Ingeniería en Sistemas
- **Curso:** Programación II
- **Estudiante:** Hans Douglas Edenilzon Alvarado Milian
- **Carné:** 9941-25-17016
- **Proyecto:** PF-12 — Agenda de Clínica Dental
- **Etapa:** Entrega final

## 1. Descripción del proyecto

En este proyecto desarrollé una aplicación web para organizar los tratamientos y las citas de una clínica dental, con un sistema que permite registrar tratamientos,
administrar citas, consultar los horarios y llevar un control de las atenciones realizadas, pero para evitar horarios duplicados se incorporó las validaciones, poder impedir 
reservaciones en fechas pasadas y conservar el historial de las citas.

Después de la evaluación final, realicé mejoras adicionales en la base de datos, las reglas del negocio y las pruebas automatizadas.

## 2. Objetivo

Desarrollar una aplicación web utilizando Java, Spring Boot y PostgreSQL para administrar tratamientos y citas dentales mediante una API REST y una interfaz sencilla.

## 3. Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- HTML, CSS y JavaScript
- JUnit 5
- Mockito
- Postman
- IntelliJ IDEA
- Git y GitHub

## 4. Organización del proyecto

El proyecto utiliza una arquitectura por capas:

| Carpeta | Función |
|---|---|
| modelo | Entidades JPA |
| repositorio | Consultas a PostgreSQL |
| servicio | Reglas del negocio |
| controlador | Endpoints REST |
| dto | Transferencia y validación de datos |
| exception | Manejo centralizado de excepciones |
| resources/static | Interfaz web |
| sql | Scripts de base de datos |
| postman | Colección de solicitudes |
| evidencias | Documentación y capturas |
| src/test | Pruebas automatizadas |

## 5. Entidades y relaciones

El sistema utiliza dos entidades principales.

### Tratamiento

Campos:

- id
- nombre
- descripcion
- precio

### Cita

Campos:

- id
- paciente
- fecha
- hora
- tratamiento
- precioAcordado
- observaciones
- estado

Cada cita pertenece a un tratamiento mediante una relación `@ManyToOne`.

El campo `paciente` almacena directamente el nombre de la persona que solicita la cita.

Los estados permitidos son:

- PROGRAMADA
- ATENDIDA
- CANCELADA

## 6. Funcionalidades

El sistema permite registrar, consultar, actualizar y eliminar tratamientos cuando no tengan citas relacionadas 
así como también permite registrar citas, actualizar su información, consultar la agenda y cambiar su estado.

Incluye consultas por fecha, por tratamiento junto con una agenda de citas pendientes y un indicador que calcula el importe
estimado de los tratamientos correspondientes a las citas programadas.

## 7. Reglas del negocio

1. No se permiten dos citas no canceladas con la misma fecha y hora.
2. Las citas nuevas deben registrarse con estado PROGRAMADA.
3. No se permite registrar ni reprogramar citas en fechas u horarios pasados.
4. Cada cita debe tener un tratamiento existente.
5. Las citas atendidas y canceladas no pueden modificarse.
6. Las citas canceladas conservan su historial.
7. Una cita cancelada libera su horario.
8. No se permite eliminar tratamientos que tengan citas relacionadas.
9. Cada cita conserva el precio acordado al momento de su registro.
10. Al cambiar el tratamiento de una cita programada, se asigna el precio del nuevo tratamiento.

## 8. Correcciones realizadas después de la evaluación final

### Validación de fechas
Se incorporó una validación que compara la fecha y hora de la cita con el momento actual y si una cita intenta registrarse o reprogramarse en el pasado,
la API devuelve HTTP 400 con un mensaje comprensible.

### Protección contra horarios duplicados
Además de comprobar la disponibilidad desde el servicio, se creó un índice único parcial en PostgreSQL.
El cual es `idx_citas_horario_activo` impidiendo que dos citas no canceladas ocupen el mismo horario.

También se incorporó el manejo de errores de integridad para responder adecuadamente ante conflictos de unicidad.

### Conservación del precio acordado
Se agregó el campo `precio_acordado` a la tabla de citas.
El indicador utiliza este precio guardado, evitando que los cambios posteriores en el catálogo modifiquen el importe de las citas existentes.

### Simplificación del modelo de pacientes
Se eliminó la entidad auxiliar Paciente y sus componentes independientes, conservando las dos entidades principales, Cita y Tratamiento, dejando
citas para almacenar directamente los nombres

### Pruebas automatizadas
Se creó `CitaServicioTest.java` utilizando JUnit 5 y Mockito.

## 9. Base de datos
El proyecto utiliza PostgreSQL.

Base de datos principal:

`agenda_clinica_dental`

Archivos incluidos:

- `sql/01_estructura.sql`
- `sql/02_datos_prueba.sql`

El primer archivo crea las tablas y el índice único de horarios.

El segundo contiene cuatro tratamientos y diez citas de ejemplo, incluyendo sus precios acordados.

Los scripts están preparados para utilizarse al configurar una base de datos nueva. Los datos de prueba no deben insertarse repetidamente en una base que ya contiene registros.

## 10. Configuración y ejecución
Requisitos:

- Java 21
- Maven
- PostgreSQL
- IntelliJ IDEA o un entorno compatible

Pasos:

1. Clonar o descargar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Configurar PostgreSQL.
4. Crear la base de datos `agenda_clinica_dental`.
5. Ejecutar los scripts SQL en una base de datos nueva.
6. Configurar la variable de entorno `DB_PASSWORD` con la contraseña local de PostgreSQL.
7. Esperar a que Maven cargue las dependencias.
8. Ejecutar `AgendaClinicaDentalApplication`.
9. Abrir `http://localhost:8080/` en el navegador.

El frontend está integrado con Spring Boot y no necesita un servidor adicional.

La contraseña de PostgreSQL no se almacena directamente en el repositorio.

## 11. Endpoints REST

### Tratamientos

| Método | Endpoint | Función |
|---|---|---|
| GET | /api/tratamientos | Listar tratamientos |
| GET | /api/tratamientos/{id} | Consultar tratamiento |
| POST | /api/tratamientos | Registrar tratamiento |
| PUT | /api/tratamientos/{id} | Actualizar tratamiento |
| DELETE | /api/tratamientos/{id} | Eliminar tratamiento permitido |

### Citas

| Método | Endpoint | Función |
|---|---|---|
| GET | /api/citas | Listar citas |
| GET | /api/citas/{id} | Consultar cita |
| POST | /api/citas | Registrar cita |
| PUT | /api/citas/{id} | Actualizar cita programada |
| PATCH | /api/citas/{id}/estado | Cambiar estado |
| DELETE | /api/citas/{id} | Cancelar cita |
| GET | /api/citas/por-fecha | Filtrar por fecha |
| GET | /api/citas/por-tratamiento/{id} | Filtrar por tratamiento |
| GET | /api/citas/pendientes | Consultar agenda pendiente |
| GET | /api/citas/total-estimado | Obtener total estimado |

La API utiliza DTO para recibir y devolver los datos necesarios.

## 12. Frontend
La interfaz se desarrolló utilizando HTML, CSS y JavaScript, esta queda integrada con Spring Boot mediante solicitudes HTTP,
permitiendo administrar los tratamientos, registrar citas, consultar información, cambiar estados y utilizar filtros.

## 13. Pruebas realizadas
Se realizaron pruebas manuales utilizando IntelliJ IDEA, el archivo `pruebas.http`, el navegador y Postman.

Entre las comprobaciones realizadas se encuentran:

- Consulta de tratamientos y citas.
- Registro de citas.
- Búsqueda por identificador.
- Consultas por fecha y tratamiento.
- Agenda pendiente.
- Cálculo del total estimado.
- Rechazo de horarios ocupados.
- Rechazo de fechas pasadas.
- Validación de campos obligatorios.
- Validación de estados.
- Respuesta ante registros inexistentes.

### Resultados HTTP

| Operación | Respuesta |
|---|---|
| Consulta correcta | 200 OK |
| Registro correcto | 201 Created |
| Datos inválidos | 400 Bad Request |
| Cita inexistente | 404 Not Found |
| Horario duplicado | 409 Conflict |

Se ejecutaron 14 solicitudes HTTP de comprobación, obteniendo las respuestas esperadas.

### Pruebas automatizadas

Se creó la clase `CitaServicioTest.java`.

Las ocho pruebas verifican:

1. Rechazo de horarios ocupados.
2. Rechazo de fechas pasadas.
3. Registro del precio acordado.
4. Conservación del precio histórico.
5. Cambio de estado a ATENDIDA.
6. Cambio de estado a CANCELADA.
7. Bloqueo de modificación de una cita atendida.
8. Bloqueo de cambio de estado de una cita cancelada.

Resultado obtenido:

**8 pruebas ejecutadas y 8 superadas.**

### Verificación del indicador
Se modificó temporalmente el precio del blanqueamiento dental de Q600.00 a Q750.00, haciendo que el total estimado de las citas existentes
permaneciera en Q2,825.00, permitiendo comprobar que el indicador utiliza los precios acordados.

### Concurrencia
La base de datos utiliza un índice único parcial para impedir registros no cancelados con la misma fecha y hora.
La protección fue creada correctamente en PostgreSQL. La prueba de solicitudes verdaderamente simultáneas queda como comprobación de integración adicional.

## 14. Colección Postman
El repositorio incluye una colección de Postman con 22 solicitudes HTTP que cubren las operaciones principales del sistema.