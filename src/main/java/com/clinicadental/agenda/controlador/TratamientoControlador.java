package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.dto.TratamientoDTO;
import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.servicio.TratamientoServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoControlador {

    private final TratamientoServicio servicio;

    public TratamientoControlador(TratamientoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<TratamientoDTO> listar() {
        List<TratamientoDTO> lista = new ArrayList<>();

        for (Tratamiento tratamiento : servicio.listar()) {
            lista.add(convertirDTO(tratamiento));
        }

        return lista;
    }

    @GetMapping("/{id}")
    public TratamientoDTO buscarPorId(@PathVariable Long id) {
        return convertirDTO(servicio.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TratamientoDTO guardar(
            @Valid @RequestBody TratamientoDTO datos) {

        Tratamiento tratamiento = convertirEntidad(datos);
        return convertirDTO(servicio.guardar(tratamiento));
    }

    @PutMapping("/{id}")
    public TratamientoDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TratamientoDTO datos) {

        Tratamiento tratamiento = convertirEntidad(datos);
        return convertirDTO(servicio.actualizar(id, tratamiento));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }

    private TratamientoDTO convertirDTO(Tratamiento tratamiento) {
        TratamientoDTO dto = new TratamientoDTO();

        dto.setId(tratamiento.getId());
        dto.setNombre(tratamiento.getNombre());
        dto.setDescripcion(tratamiento.getDescripcion());
        dto.setPrecio(tratamiento.getPrecio());

        return dto;
    }

    private Tratamiento convertirEntidad(TratamientoDTO dto) {
        Tratamiento tratamiento = new Tratamiento();

        tratamiento.setNombre(dto.getNombre());
        tratamiento.setDescripcion(dto.getDescripcion());
        tratamiento.setPrecio(dto.getPrecio());

        return tratamiento;
    }
}