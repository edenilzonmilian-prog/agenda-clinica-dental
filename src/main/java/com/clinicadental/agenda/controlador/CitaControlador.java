package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.servicio.CitaServicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaControlador {

    private final CitaServicio servicio;

    public CitaControlador(CitaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Cita> listar() {
        return servicio.listar();
    }

    @GetMapping("/por-fecha")
    public List<Cita> listarPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        return servicio.listarPorFecha(fecha);
    }

    @GetMapping("/por-tratamiento/{tratamientoId}")
    public List<Cita> listarPorTratamiento(
            @PathVariable Long tratamientoId) {
        return servicio.listarPorTratamiento(tratamientoId);
    }

    @GetMapping("/pendientes")
    public List<Cita> listarPendientes() {
        return servicio.listarPendientes();
    }

    @GetMapping("/total-estimado")
    public BigDecimal calcularTotalEstimado() {
        return servicio.calcularTotalEstimadoProgramadas();
    }

    @GetMapping("/{id}")
    public Cita buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cita guardar(@RequestBody Cita cita) {
        return servicio.guardar(cita);
    }

    @PutMapping("/{id}")
    public Cita actualizar(
            @PathVariable Long id,
            @RequestBody Cita cita) {
        return servicio.actualizar(id, cita);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}