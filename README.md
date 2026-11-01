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

En este proyecto desarrollé una agenda para una clínica dental utilizando Java, Spring Boot, PostgreSQL, HTML, CSS y JavaScript; el sistema permite administrar tratamientos y citas dentales, registrar pacientes mediante su nombre, consultar los 
horarios disponibles y llevar un control de las atenciones. Una de las funciones principales es evitar que se registren dos citas activas en la misma fecha y hora permitiendo cambiar el estado de las citas y conservar su información histórica.

## 2. Objetivo

Desarrollar una aplicación web conectada a una API REST que permita administrar los tratamientos y citas de una clínica dental, utilizando PostgreSQL para almacenar la información.

## 3. Tecnologías utilizadas

- **Java:** desarrollo del backend.
- **Spring Boot:** creación de la API REST.
- **Spring Data JPA y Hibernate:** conexión y operaciones con PostgreSQL.
- **Maven:** administración de dependencias.
- **PostgreSQL:** almacenamiento de información.
- **HTML:** estructura de la interfaz web.
- **CSS:** diseño de la interfaz.
- **JavaScript:** comunicación entre el frontend y la API.
- **IntelliJ IDEA:** desarrollo y pruebas del proyecto.
- **Postman:** pruebas de los endpoints.
- **Git y GitHub:** control de versiones.

## 4. Desarrollo del proyecto

### Primer avance

Durante el primer avance desarrollé la estructura inicial del proyecto, incluyendo las entidades, los controladores, servicios y repositorios. También establecí la conexión con PostgreSQL y las operaciones principales de tratamientos y citas.

### Segundo avance

En el segundo avance mejoré la validación de horarios mediante consultas directas al repositorio, así mismo implemente las consultas por fecha y tratamiento, la agenda pendiente y el cálculo del total estimado junto con el DTO, las validaciones de datos y un manejador 
centralizado de excepciones para devolver mensajes comprensibles.

### Entrega final

Para la entrega final completé las correcciones indicadas por el ingeniero David Alvarez y desarrollé la interfaz utilizando HTML, CSS y JavaScript, separando la actualización de los datos de las citas de los cambios de estado,
permitiendo marcar las citas como atendidas o canceladas mediante una operación independiente.

También modifiqué la lógica para conservar el historial de las citas y evitar la eliminación de tratamientos que tengan citas relacionadas; Finalmente, conecté la interfaz con el backend y realicé pruebas utilizando el navegador, IntelliJ IDEA y Postman.

## 5. Organización del proyecto

El proyecto está organizado de la siguiente manera:

| Carpeta | Función |
|---|---|
| modelo | Entidades del sistema |
| repositorio | Acceso a PostgreSQL |
| servicio | Operaciones y reglas de negocio |
| controlador | Endpoints de la API |
| dto | Transferencia y validación de datos |
| exception | Manejo centralizado de errores |
| resources/static | Archivos HTML, CSS y JavaScript |
| sql | Scripts de base de datos |
| postman | Colección de solicitudes HTTP |
| evidencias | Capturas y documentación de pruebas |

## 6. Entidades y relaciones

### Tratamiento

Contiene los campos:

- id
- nombre
- descripcion
- precio

### Cita

Contiene los campos:

- id
- paciente
- fecha
- hora
- tratamiento
- observaciones
- estado

Los estados utilizados son `PROGRAMADA`, `ATENDIDA` y `CANCELADA`.

Cada cita corresponde a un tratamiento mediante una relación `@ManyToOne`.

También se conserva una entidad auxiliar Paciente. Sin embargo, las citas registran el nombre del paciente directamente, por lo que no existe una relación JPA entre ambas entidades.

## 7. Reglas de negocio

El sistema implementa las siguientes reglas:

1. No permite registrar dos citas activas con la misma fecha y hora.
2. Cada cita debe tener un tratamiento registrado.
3. Las citas nuevas se registran con estado `PROGRAMADA`.
4. Las citas programadas pueden modificarse o cambiar de estado.
5. Las citas atendidas y canceladas no pueden modificarse.
6. La cancelación conserva el historial de la cita.
7. Los horarios de las citas canceladas pueden utilizarse nuevamente.
8. No se permite eliminar un tratamiento que tenga citas relacionadas.

## 8. Base de datos

El proyecto utiliza PostgreSQL.

**Base de datos principal:** `agenda_clinica_dental`

La conexión está configurada en `application.properties`, utilizando el puerto 5432 y la variable de entorno `DB_PASSWORD`.

Se incluyen los siguientes archivos:

- `sql/01_estructura.sql`: creación de las tablas.
- `sql/02_datos_prueba.sql`: registros de prueba.

También se utilizó una base independiente llamada `agenda_clinica_dental_prueba` para comprobar los scripts SQL sin afectar la base principal, preparando los 4 tratamientos y las 10 citas de prueba.

## 9. Instrucciones para ejecutar el proyecto

1. Descargar o clonar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Esperar a que Maven cargue las dependencias.
4. Tener PostgreSQL instalado y funcionando.
5. Crear la base de datos `agenda_clinica_dental`.
6. Configurar la conexión y la variable `DB_PASSWORD`.
7. Ejecutar `AgendaClinicaDentalApplication`.
8. Abrir `http://localhost:8080/` desde el navegador.

La aplicación utiliza el puerto 8080 y sirve el frontend desde Spring Boot, sin necesidad de iniciar otro servidor.

## 10. Endpoints principales

### Tratamientos

| Método | Endpoint | Función |
|---|---|---|
| GET | /api/tratamientos | Listar tratamientos |
| GET | /api/tratamientos/{id} | Consultar tratamiento |
| POST | /api/tratamientos | Registrar tratamiento |
| PUT | /api/tratamientos/{id} | Actualizar tratamiento |
| DELETE | /api/tratamientos/{id} | Eliminar tratamiento sin citas asociadas |

### Citas

| Método | Endpoint | Función |
|---|---|---|
| GET | /api/citas | Listar citas |
| GET | /api/citas/{id} | Consultar cita |
| POST | /api/citas | Registrar cita |
| PUT | /api/citas/{id} | Actualizar cita programada |
| PATCH | /api/citas/{id}/estado | Cambiar estado |
| DELETE | /api/citas/{id} | Cancelar cita conservando su historial |
| GET | /api/citas/por-fecha | Consultar citas por fecha |
| GET | /api/citas/por-tratamiento/{id} | Consultar por tratamiento |
| GET | /api/citas/pendientes | Consultar agenda pendiente |
| GET | /api/citas/total-estimado | Calcular total estimado |

## 11. Interfaz web

Para la entrega final desarrollé una interfaz sencilla utilizando tres archivos:

- `index.html`: estructura de la página.
- `styles.css`: diseño visual.
- `app.js`: conexión con la API REST.

La interfaz permite registrar y modificar tratamientos, administrar citas, cambiar sus estados, consultar la agenda y filtrar por fecha o tratamiento.

## 12. Pruebas realizadas

Realicé pruebas utilizando el archivo `pruebas.http`, la interfaz web y Postman.

Entre los resultados comprobados se encuentran:

| Prueba | Resultado |
|---|---|
| Consultar tratamientos | 200 OK |
| Registrar tratamientos | Correcto |
| Actualizar tratamientos | Correcto |
| Registrar citas | Correcto |
| Actualizar citas programadas | Correcto |
| Registrar una cita en horario ocupado | 409 Conflict |
| Cancelar una cita | Correcto |
| Reutilizar un horario cancelado | Correcto |
| Marcar una cita como atendida | Correcto |
| Consultar citas por fecha | Correcto |
| Consultar citas por tratamiento | Correcto |
| Consultar agenda pendiente | Correcto |
| Calcular total estimado | Correcto |
| Enviar un estado inválido | 400 Bad Request |
| Cambiar el estado de una cita atendida | 409 Conflict |
| Modificar una cita atendida | 409 Conflict |
| Eliminar un tratamiento con citas | Operación rechazada |

Durante las pruebas de integración registré 4 tratamientos y 10 citas, mostrando un total estimado de **Q2,225.00**.

### Postman

La colección que contiene 22 solicitudes HTTP fue importada correctamente en Postman y se comprobó la consulta de tratamientos, obteniendo una respuesta `200 OK` y una prueba automática correcta.

## 13. Evidencias
Las evidencias permiten comprobar el funcionamiento del backend, la conexión con PostgreSQL, las validaciones y la integración con el frontend.

## 14. Control de versiones

Utilicé Git y GitHub para guardar los avances del proyecto y registrar las modificaciones realizadas, manteniendo el desarrolloo en el mismo repositorio durante las diferentes entregas.

## 15. Conclusión

Durante el desarrollo de este proyecto trabajé la API REST conectada a PostgreSQL y se organizaron las operaciones mediante controladores, servicios, repositorios y DTO, así como también apliqué las observaciones realizadas por el catedrático para mejorar las validaciones,
el manejo de errores y las reglas de negocio. 

En la entrega final completé la interfaz web y comprobé su integración con el backend, permitiendo administrar los tratamientos y las citas desde una misma página, considero que el proyecto me ayudó a comprender mejor cómo se conectan las diferentes partes de una aplicación y la 
importancia de realizar pruebas antes de entregar un sistema.