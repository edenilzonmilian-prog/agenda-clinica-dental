
package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Paciente;
import com.clinicadental.agenda.repositorio.PacienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository repositorio;

    public PacienteService(PacienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Paciente> listarPacientes() {
        return repositorio.findAll();
    }

    public Paciente guardarPaciente(Paciente paciente) {
        return repositorio.save(paciente);
    }

    public Optional<Paciente> buscarPaciente(Long id) {
        return repositorio.findById(id);
    }

    public void eliminarPaciente(Long id) {
        repositorio.deleteById(id);
    }
}
