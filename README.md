# PF-12 — Agenda de Clínica Dental

## Portada

- **Universidad:** Universidad Mariano Gálvez de Guatemala
- **Carrera:** Ingeniería en Sistemas
- **Curso:** Programación II
- **Estudiante:** Hans Douglas Edenilzon Alvarado Milian
- **Carné:** 9941-25-17016
- **Proyecto asignado:** PF-12 — Agenda de Clínica Dental
- **Etapa actual:** Segundo avance

## 1. Descripción del proyecto

En este proyecto estoy desarrollando una agenda para una clínica dental utilizando Java, Spring Boot y PostgreSQL 
para poder llevar un control de los tratamientos y las citas de los pacientes, permitiendo registrar la información, consultarla y realizar cambios cuando sea necesario.

Una de las funciones principales es evitar que se registren dos citas activas en la misma fecha y hora así como también se pueden consultar las citas según su fecha, tratamiento o estado.

## 2. Objetivo

Desarrollar una aplicación que permita administrar los tratamientos y las citas dentales mediante una API REST, utilizando PostgreSQL para almacenar la información.
El sistema debe permitir organizar las citas, relacionarlas con tratamientos, controlar sus estados y evitar conflictos de horarios.

## 3. Tecnologías utilizadas

Para desarrollar el proyecto utilicé las siguientes herramientas:

- **Java:** lenguaje de programación utilizado para desarrollar el backend.
- **Spring Boot:** utilizado para crear la aplicación y los endpoints REST.
- **Maven:** administra las dependencias del proyecto.
- **PostgreSQL:** base de datos donde se almacena la información.
- **Spring Data JPA:** permite realizar operaciones con la base de datos mediante repositorios.
- **Hibernate:** permite relacionar las entidades Java con las tablas de PostgreSQL.
- **IntelliJ IDEA:** entorno donde desarrollé el proyecto y realicé las pruebas.
- **Git y GitHub:** utilizados para registrar los cambios y guardar el proyecto.

## 4. Desarrollo del proyecto

### Primer avance

Durante el primer avance trabajé en la estructura inicial del proyecto y en la conexión con PostgreSQL creando las entidades Paciente, Tratamiento y Cita, junto con sus repositorios,
servicios y controladores. También implementé las operaciones principales para registrar, consultar, actualizar y eliminar tratamientos y citas.
Una de las primeras validaciones fue impedir que dos citas activas utilizaran la misma fecha y hora.

### Segundo avance

Para este avance tomé en cuenta las observaciones realizadas por el ingeniero mejorando la validación de horarios para consultar directamente el repositorio,
sin tener que recorrer todas las citas registradas, para luego implementar las consultas por fecha y tratamiento, la agenda de citas pendientes y el cálculo del total
estimado de tratamientos programados. También incorporé reglas para controlar los cambios de estado de las citas y evitar que se modifiquen citas que
ya fueron atendidas o canceladas.

Otra de las correcciones fue implementar las clases `CitaDTO` y `TratamientoDTO`, con el objetivo de controlar los datos enviados y recibidos por la API, terminando con las validaciones para los
campos obligatorios y un manejador centralizado de excepciones, que permite devolver mensajes más claros cuando ocurre algún error.

## 5. Organización del proyecto

El proyecto está organizado en paquetes para separar las diferentes partes del backend.

| Paquete | Función |
|---|---|
| modelo | Contiene las entidades del sistema. |
| repositorio | Permite consultar y guardar información en PostgreSQL. |
| servicio | Contiene las operaciones y reglas de negocio. |
| controlador | Recibe las solicitudes HTTP. |
| dto | Controla los datos que recibe y devuelve la API. |
| exception | Contiene el manejo centralizado de excepciones. |

Esta estructura permite mantener el código organizado y facilita realizar modificaciones.

## 6. Entidades y relación

### Tratamiento

Representa los tratamientos que ofrece la clínica dental.

Sus atributos son:

- id
- nombre
- descripcion
- precio

### Cita

Almacena la información correspondiente a cada cita dental.

Sus atributos son:

- id
- paciente
- fecha
- hora
- tratamiento
- observaciones
- estado

Los estados utilizados son `PROGRAMADA`, `ATENDIDA` y `CANCELADA`.

### Paciente

Es una entidad auxiliar que permite registrar y consultar información de los pacientes.

### Relación entre las entidades

Un tratamiento puede estar relacionado con varias citas, pero cada cita corresponde a un tratamiento específico.

La relación está implementada mediante `@ManyToOne` y `@JoinColumn`.

Actualmente, la entidad Cita almacena el nombre del paciente como texto, por lo que no existe una relación JPA directa entre Cita y Paciente.

## 7. Base de datos

Para almacenar la información utilizo PostgreSQL y la base de datos principal se llama:
`agenda_clinica_dental`

La conexión se encuentra configurada en `application.properties`, utilizando el puerto 5432 y la variable de entorno `DB_PASSWORD` para la contraseña.

Durante este avance también preparé dos archivos SQL:

- `sql/01_estructura.sql`: contiene las instrucciones para crear las tablas.
- `sql/02_datos_prueba.sql`: contiene los registros utilizados para comprobar la base de datos.

Para realizar las pruebas creé una base independiente llamada:

`agenda_clinica_dental_prueba`

En ella comprobé la creación de las tres tablas y la inserción de 4 tratamientos y 10 citas dentales.

Estas pruebas se realizaron sin modificar los registros de la base principal.

## 8. Instrucciones para ejecutar el proyecto

1. Descargar o clonar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Esperar a que Maven cargue las dependencias.
4. Crear la base de datos PostgreSQL `agenda_clinica_dental`.
5. Configurar la conexión y la variable `DB_PASSWORD`.
6. Ejecutar la clase `AgendaClinicaDentalApplication`.
7. Esperar a que Spring Boot inicie correctamente.

La aplicación utiliza el puerto 8080.

Dirección base:

`http://localhost:8080`

## 9. Endpoints principales

### Pacientes

| Método | Endpoint | Función |
|---|---|---|
| POST | /api/pacientes | Registrar paciente |
| GET | /api/pacientes | Consultar pacientes |

### Tratamientos

| Método | Endpoint | Función |
|---|---|---|
| POST | /api/tratamientos | Registrar tratamiento |
| GET | /api/tratamientos | Consultar tratamientos |
| GET | /api/tratamientos/{id} | Buscar tratamiento |
| PUT | /api/tratamientos/{id} | Actualizar tratamiento |
| DELETE | /api/tratamientos/{id} | Eliminar tratamiento |

### Citas

| Método | Endpoint | Función |
|---|---|---|
| POST | /api/citas | Registrar cita |
| GET | /api/citas | Consultar citas |
| GET | /api/citas/{id} | Buscar cita |
| PUT | /api/citas/{id} | Actualizar cita |
| DELETE | /api/citas/{id} | Eliminar cita |
| GET | /api/citas/por-fecha?fecha=AAAA-MM-DD | Consultar por fecha |
| GET | /api/citas/por-tratamiento/{id} | Consultar por tratamiento |
| GET | /api/citas/pendientes | Consultar agenda pendiente |
| GET | /api/citas/total-estimado | Calcular total estimado |

## 10. Pruebas realizadas

Para comprobar el funcionamiento del backend utilicé el archivo `pruebas.http` de IntelliJ IDEA.

Durante el primer avance realicé las pruebas básicas de registro y consulta.

En el segundo avance agregué nuevas solicitudes para comprobar las consultas, los cambios de estado, las validaciones y el manejo de errores.

Entre los resultados obtenidos se encuentran:

| Prueba | Resultado |
|---|---|
| Registrar tratamiento | 201 Created |
| Consultar tratamientos | 200 OK |
| Registrar cita dental | 201 Created |
| Consultar citas | 200 OK |
| Registrar una cita en horario ocupado | 409 Conflict |
| Consultar total estimado | 200 OK |
| Actualizar una cita | 200 OK |
| Registrar una cita utilizando DTO | 201 Created |
| Actualizar una cita utilizando DTO | 200 OK |
| Enviar datos obligatorios incorrectos | 400 Bad Request |
| Buscar una cita inexistente | 404 Not Found |
| Intentar modificar una cita atendida | 409 Conflict |

También comprobé que el sistema devuelve mensajes de error más comprensibles gracias al manejo centralizado de excepciones.

### Pruebas SQL

Para comprobar los scripts SQL utilicé una base de datos independiente.

Los resultados fueron:

- Creación correcta de 3 tablas.
- Inserción correcta de 4 tratamientos.
- Inserción correcta de 10 citas.
- Consultas SQL ejecutadas sin errores.

De esta manera pude comprobar que los scripts de estructura y datos de prueba funcionan correctamente.

## 11. Estado actual del proyecto

Actualmente el proyecto cuenta con un backend desarrollado en Spring Boot y conectado a PostgreSQL y las operaciones principales de tratamientos o citas están implementadas con las consultas específicas del PF-12.

También se incorporaron los DTO, las validaciones de datos, las reglas de cambios de estado y el manejo centralizado de excepciones.

Además, se prepararon y probaron los scripts SQL solicitados.

El proyecto todavía se encuentra en desarrollo, ya que falta completar la interfaz y realizar las pruebas finales de integración.

## 12. Trabajo pendiente

Para la entrega final tengo previsto trabajar en:

- Desarrollar la interfaz utilizando HTML, CSS y JavaScript.
- Conectar la interfaz con los endpoints del backend.
- Realizar las pruebas finales del funcionamiento completo.
- Completar las últimas evidencias y la documentación de entrega.

La intención es aprovechar el backend que ya está desarrollado y continuar con lo que falta sin agregar funcionalidades fuera de los requisitos del PF-12.

## 13. Control de versiones

Para llevar el control del desarrollo utilizo Git y GitHub registré los commits correspondientes a las consultas, la optimización de horarios, las reglas de cambios de estado, los DTO, el manejo de excepciones y los scripts SQL.
Esto permite identificar los cambios realizados durante cada etapa del proyecto.

## 14. Conclusión del segundo avance

Durante este segundo avance pude mejorar varias partes del sistema a partir de las observaciones del ingeniero David Alvarez.
Uno de los cambios más importantes fue mejorar la validación de horarios e implementar las consultas necesarias para organizar las citas dentales, aprendí a utilizar DTO para controlar la información de la API y a manejar las excepciones
para que el sistema devuelva mensajes más comprensibles.
Otra parte importante fue trabajar directamente con PostgreSQL, creando los scripts SQL y comprobando los registros en una base de pruebas independiente y aunque
todavía quedan algunas partes por desarrollar, principalmente la interfaz y las pruebas finales; considero enviar un tercer avance con el código terminado para luego subirlo al portal de canvas.