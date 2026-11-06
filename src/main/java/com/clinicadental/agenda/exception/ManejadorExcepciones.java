package com.clinicadental.agenda.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validarDatos(
            MethodArgumentNotValidException ex) {

        String campo = ex.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField())
                .orElse("datos");

        return ResponseEntity.badRequest().body(
                Map.of("mensaje", "El campo " + campo + " no es valido")
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarError(
            ResponseStatusException ex) {

        String mensaje = ex.getReason() != null
                ? ex.getReason()
                : "No se pudo completar la solicitud";

        return ResponseEntity.status(ex.getStatusCode()).body(
                Map.of("mensaje", mensaje)
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> manejarIntegridad(
            DataIntegrityViolationException ex) {

        Throwable causa = ex;

        while (causa != null) {
            if (causa instanceof SQLException errorSQL
                    && "23505".equals(errorSQL.getSQLState())) {

                String mensaje = ex.getMessage() != null
                        && ex.getMessage().contains("idx_citas_horario_activo")
                        ? "Ya existe una cita activa en esa fecha y hora"
                        : "Ya existe un registro con esos datos";

                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        Map.of("mensaje", mensaje)
                );
            }

            causa = causa.getCause();
        }

        return ResponseEntity.badRequest().body(
                Map.of("mensaje", "La operacion incumple una restriccion de la base de datos")
        );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Map<String, String>> datosIncorrectos(
            Exception ex) {

        return ResponseEntity.badRequest().body(
                Map.of("mensaje", "Los datos enviados no son validos")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> errorGeneral(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("mensaje", "Error interno del servidor")
        );
    }
}