package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.servicio.CitaServicio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaControlador {

    private final CitaServicio servicio;

    public CitaControlador(CitaServicio servicio) {
        this.servicio = servicio;
    }

    // Consultar todas las citas
    @GetMapping
    public List<Cita> listar() {
        return servicio.listar();
    }

    // Consultar cita por ID
    @GetMapping("/{id}")
    public Cita buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    // Registrar nueva cita
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cita guardar(@RequestBody Cita cita) {
        return servicio.guardar(cita);
    }

    // Modificar cita existente
    @PutMapping("/{id}")
    public Cita actualizar(
            @PathVariable Long id,
            @RequestBody Cita cita) {

        return servicio.actualizar(id, cita);
    }

    // Eliminar cita
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}