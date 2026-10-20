
package com.clinicadental.agenda.controlador;

import com.clinicadental.agenda.modelo.Paciente;
import com.clinicadental.agenda.servicio.PacienteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService servicio;

    public PacienteController(PacienteService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Paciente> listar() {
        return servicio.listarPacientes();
    }

    @PostMapping
    public Paciente guardar(@RequestBody Paciente paciente) {
        return servicio.guardarPaciente(paciente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Long id) {
        return servicio.buscarPaciente(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (servicio.buscarPaciente(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        servicio.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}
