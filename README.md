# PF-12 — Agenda de Clínica Dental

## Portada

- **Universidad:** Universidad Mariano Gálvez de Guatemala
- **Carrera:** Ingeniería en Sistemas
- **Curso:** Programación II
- **Estudiante:** Hans Douglas Edenilzon Alvarado Milian
- **Carné:** 9941-25-17016
- **Proyecto asignado:** PF-12 — Agenda de Clínica Dental
- **Entrega:** Primer avance

## 1. Descripción del proyecto

En este proyecto estoy desarrollando una agenda para una clínica dental utilizando Java, Spring Boot y PostgreSQL.

La idea principal es llevar un control de los tratamientos y las citas de los pacientes, permitiendo registrar la información, consultarla y realizar cambios cuando sea necesario.

Una de las funciones importantes del sistema es evitar que se registren dos citas activas en la misma fecha y hora, para mantener una mejor organización de las atenciones.

## 2. Objetivo

Desarrollar una aplicación que permita administrar los tratamientos y las citas dentales mediante una API REST, utilizando una base de datos PostgreSQL para guardar la información.

El sistema debe permitir organizar las citas, relacionarlas con tratamientos y aplicar las reglas necesarias para evitar conflictos de horarios.

## 3. Tecnologías utilizadas

Para desarrollar el proyecto utilicé las siguientes herramientas:

- **Java:** lenguaje de programación utilizado para desarrollar el backend.
- **Spring Boot:** framework utilizado para crear la aplicación y los endpoints REST.
- **Maven:** herramienta para administrar las dependencias del proyecto.
- **PostgreSQL:** base de datos utilizada para almacenar la información.
- **Spring Data JPA:** permite realizar las operaciones con la base de datos mediante repositorios.
- **Hibernate:** se encarga del mapeo de las entidades Java a las tablas de PostgreSQL.
- **IntelliJ IDEA:** entorno de desarrollo utilizado para programar y realizar las pruebas.
- **Git y GitHub:** herramientas utilizadas para llevar el control de versiones y almacenar el proyecto.

## 4. Desarrollo del primer avance

Durante este primer avance trabajé principalmente en la creación del proyecto, su estructura y la conexión con PostgreSQL.

Primero configuré Spring Boot y Maven. Después creé las entidades necesarias para representar la información de pacientes, tratamientos y citas.

También implementé los repositorios, servicios y controladores para realizar las primeras operaciones mediante una API REST.

Las principales funcionalidades desarrolladas son:

**Pacientes**
- Registrar información de pacientes.
- Consultar los pacientes registrados.

**Tratamientos**
- Registrar nuevos tratamientos.
- Consultar los tratamientos almacenados.
- Buscar tratamientos por su identificador.
- Actualizar y eliminar tratamientos mediante los métodos implementados.

**Citas dentales**
- Registrar nuevas citas.
- Consultar las citas almacenadas.
- Relacionar cada cita con un tratamiento.
- Registrar el estado de cada cita.
- Validar que no exista otra cita activa en la misma fecha y hora.
- Actualizar y eliminar citas mediante los métodos implementados.

Las operaciones de actualización y eliminación se encuentran programadas, pero todavía no se han incluido en las pruebas funcionales documentadas de este avance.

## 5. Organización del proyecto

El backend está dividido en paquetes para mantener el código organizado.

| Paquete | Función |
|---|---|
| modelo | Contiene las clases que representan las entidades. |
| repositorio | Permite realizar operaciones con PostgreSQL mediante JPA. |
| servicio | Contiene las operaciones y validaciones básicas. |
| controlador | Recibe las solicitudes HTTP y devuelve las respuestas. |

Esta organización permite separar las responsabilidades y facilita realizar modificaciones al sistema durante los siguientes avances.

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

### Paciente

Es una entidad auxiliar utilizada para registrar y consultar información de pacientes.

### Relación entre las entidades principales

Un tratamiento puede estar relacionado con varias citas, mientras que cada cita corresponde a un tratamiento específico.

Esta relación se implementó mediante las anotaciones `@ManyToOne` y `@JoinColumn`.

Por el momento, la clase Cita almacena el nombre del paciente como texto, por lo que todavía no existe una relación JPA directa entre Cita y Paciente.

## 7. Base de datos

Para almacenar la información se utiliza PostgreSQL.

La base de datos local se llama:

`agenda_clinica_dental`

La conexión está configurada en el archivo `application.properties`, utilizando el puerto 5432 de PostgreSQL.

La contraseña se obtiene mediante la variable de entorno `DB_PASSWORD`, evitando escribirla directamente en el código fuente.

Durante el desarrollo comprobé que los registros permanecen guardados después de detener y volver a ejecutar Spring Boot.

## 8. Instrucciones para ejecutar el proyecto

Para ejecutar la aplicación se necesita tener instalado Java, PostgreSQL y un entorno compatible con proyectos Maven.

Los pasos son:

1. Descargar o clonar el repositorio.
2. Abrir la carpeta del proyecto en IntelliJ IDEA.
3. Esperar a que Maven cargue las dependencias.
4. Crear una base de datos PostgreSQL llamada `agenda_clinica_dental`.
5. Configurar la conexión local de PostgreSQL y la variable de entorno `DB_PASSWORD`.
6. Ejecutar la clase `AgendaClinicaDentalApplication`.
7. Esperar a que Spring Boot inicie correctamente.

La aplicación utiliza el puerto 8080.

Dirección base de la API:

`http://localhost:8080`

## 9. Endpoints principales

Los siguientes endpoints están implementados en el proyecto:

| Método | Endpoint | Función |
|---|---|---|
| POST | /api/pacientes | Registrar paciente |
| GET | /api/pacientes | Consultar pacientes |
| POST | /api/tratamientos | Registrar tratamiento |
| GET | /api/tratamientos | Consultar tratamientos |
| GET | /api/tratamientos/{id} | Buscar tratamiento |
| PUT | /api/tratamientos/{id} | Actualizar tratamiento |
| DELETE | /api/tratamientos/{id} | Eliminar tratamiento |
| POST | /api/citas | Registrar cita |
| GET | /api/citas | Consultar citas |
| GET | /api/citas/{id} | Buscar cita |
| PUT | /api/citas/{id} | Actualizar cita |
| DELETE | /api/citas/{id} | Eliminar cita |

## 10. Pruebas realizadas

Para comprobar el funcionamiento del sistema utilicé el archivo `pruebas.http` de IntelliJ IDEA.

Realicé solicitudes HTTP para registrar y consultar tratamientos y citas.

| Prueba | Resultado |
|---|---|
| Registrar un tratamiento | 201 Created |
| Consultar tratamientos | 200 OK |
| Registrar una cita dental | 201 Created |
| Consultar citas | 200 OK |
| Intentar registrar otra cita en un horario ocupado | 409 Conflict |
| Consultar citas después de reiniciar la aplicación | 200 OK |

Una de las pruebas más importantes fue intentar registrar dos citas activas en la misma fecha y hora.

El sistema rechazó la segunda solicitud y devolvió el código HTTP 409, lo cual permitió comprobar que la validación básica de horarios funciona en ese caso.

También comprobé que la cita registrada mantenía su información y su tratamiento relacionado después de reiniciar Spring Boot.

## 11. Estado actual del proyecto

Actualmente se cuenta con una aplicación Spring Boot conectada a PostgreSQL, con las entidades principales creadas y operaciones básicas que ya fueron probadas.

El primer avance se concentra en demostrar que la conexión con la base de datos funciona, que los registros pueden almacenarse y consultarse, y que las citas están relacionadas con sus respectivos tratamientos.

## 12. Próximos avances

En los siguientes avances se continuará trabajando en:

- Incorporar DTO para recibir y devolver información.
- Mejorar las validaciones y el manejo de excepciones.
- Implementar consultas por fecha y tratamiento.
- Mostrar las citas pendientes.
- Calcular el total estimado de tratamientos programados.
- Reforzar las reglas de negocio.
- Desarrollar una interfaz utilizando HTML, CSS y JavaScript.
- Completar las pruebas y la documentación del sistema.

## 13. Control de versiones

El proyecto utiliza Git y GitHub para registrar los cambios realizados durante su desarrollo.

Se utilizará el mismo repositorio para el primer avance, segundo avance y entrega final.

Cada etapa tendrá sus respectivos commits, con el objetivo de mantener un historial de los cambios y mejoras realizadas.

## 14. Conclusión del primer avance

Durante este primer avance logré establecer la estructura inicial del sistema y conectar la aplicación con PostgreSQL.

También pude comprobar el funcionamiento de las primeras operaciones de tratamientos y citas mediante solicitudes HTTP.

Una parte importante fue verificar que las citas quedaran relacionadas con los tratamientos y que el sistema rechazara una segunda cita activa cuando el horario ya estaba ocupado.

Con estas funcionalidades se cuenta con una base para continuar desarrollando las demás características del proyecto.