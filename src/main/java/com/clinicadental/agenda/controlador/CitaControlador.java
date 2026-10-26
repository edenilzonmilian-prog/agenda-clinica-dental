package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.dto.CitaDTO;
import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.servicio.CitaServicio;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaControlador {

    private final CitaServicio servicio;

    public CitaControlador(CitaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<CitaDTO> listar() {
        return convertirLista(servicio.listar());
    }

    @GetMapping("/por-fecha")
    public List<CitaDTO> listarPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        return convertirLista(servicio.listarPorFecha(fecha));
    }

    @GetMapping("/por-tratamiento/{tratamientoId}")
    public List<CitaDTO> listarPorTratamiento(
            @PathVariable Long tratamientoId) {
        return convertirLista(servicio.listarPorTratamiento(tratamientoId));
    }

    @GetMapping("/pendientes")
    public List<CitaDTO> listarPendientes() {
        return convertirLista(servicio.listarPendientes());
    }

    @GetMapping("/total-estimado")
    public BigDecimal calcularTotalEstimado() {
        return servicio.calcularTotalEstimadoProgramadas();
    }

    @GetMapping("/{id}")
    public CitaDTO buscar(@PathVariable Long id) {
        return convertirDTO(servicio.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaDTO guardar(@Valid @RequestBody CitaDTO datos) {
        Cita cita = convertirEntidad(datos);
        return convertirDTO(servicio.guardar(cita));
    }

    @PutMapping("/{id}")
    public CitaDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaDTO datos) {
        Cita cita = convertirEntidad(datos);
        return convertirDTO(servicio.actualizar(id, cita));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }

    private List<CitaDTO> convertirLista(List<Cita> citas) {
        List<CitaDTO> lista = new ArrayList<>();

        for (Cita cita : citas) {
            lista.add(convertirDTO(cita));
        }

        return lista;
    }

    private CitaDTO convertirDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();

        dto.setId(cita.getId());
        dto.setPaciente(cita.getPaciente());
        dto.setFecha(cita.getFecha());
        dto.setHora(cita.getHora());
        dto.setTratamientoId(cita.getTratamiento().getId());
        dto.setObservaciones(cita.getObservaciones());
        dto.setEstado(cita.getEstado());

        return dto;
    }

    private Cita convertirEntidad(CitaDTO dto) {
        Cita cita = new Cita();

        cita.setPaciente(dto.getPaciente());
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setObservaciones(dto.getObservaciones());
        cita.setEstado(dto.getEstado());

        if (dto.getTratamientoId() != null) {
            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setId(dto.getTratamientoId());
            cita.setTratamiento(tratamiento);
        }

        return cita;
    }
}