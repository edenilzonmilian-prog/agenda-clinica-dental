
package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.servicio.TratamientoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoControlador {

    private final TratamientoServicio servicio;

    public TratamientoControlador(TratamientoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Tratamiento> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Tratamiento buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tratamiento guardar(@RequestBody Tratamiento tratamiento) {
        return servicio.guardar(tratamiento);
    }

    @PutMapping("/{id}")
    public Tratamiento actualizar(
            @PathVariable Long id,
            @RequestBody Tratamiento tratamiento) {
        return servicio.actualizar(id, tratamiento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}