package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.repositorio.CitaRepository;
import com.clinicadental.agenda.repositorio.TratamientoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CitaServicio {

    private final CitaRepository citaRepository;
    private final TratamientoRepository tratamientoRepository;

    public CitaServicio(
            CitaRepository citaRepository,
            TratamientoRepository tratamientoRepository) {
        this.citaRepository = citaRepository;
        this.tratamientoRepository = tratamientoRepository;
    }

    // Listar todas las citas
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    // Buscar cita por ID
    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cita no encontrada"
                ));
    }

    // Registrar una cita
    public Cita guardar(Cita cita) {
        validar(cita, null);

        Long tratamientoId = cita.getTratamiento().getId();

        Tratamiento tratamiento = tratamientoRepository
                .findById(tratamientoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tratamiento no encontrado"
                ));

        cita.setId(null);
        cita.setTratamiento(tratamiento);

        return citaRepository.save(cita);
    }

    // Actualizar cita
    public Cita actualizar(Long id, Cita datos) {
        Cita actual = buscarPorId(id);

        validar(datos, id);

        Long tratamientoId = datos.getTratamiento().getId();

        Tratamiento tratamiento = tratamientoRepository
                .findById(tratamientoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tratamiento no encontrado"
                ));

        actual.setPaciente(datos.getPaciente());
        actual.setFecha(datos.getFecha());
        actual.setHora(datos.getHora());
        actual.setTratamiento(tratamiento);
        actual.setObservaciones(datos.getObservaciones());
        actual.setEstado(datos.getEstado());

        return citaRepository.save(actual);
    }

    // Eliminar cita
    public void eliminar(Long id) {
        Cita cita = buscarPorId(id);
        citaRepository.delete(cita);
    }

    // Validar datos y evitar citas simultaneas
    private void validar(Cita cita, Long idActual) {

        if (cita == null ||
                cita.getPaciente() == null ||
                cita.getPaciente().isBlank() ||
                cita.getFecha() == null ||
                cita.getHora() == null ||
                cita.getTratamiento() == null ||
                cita.getTratamiento().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faltan datos obligatorios"
            );
        }

        if (cita.getEstado() == null ||
                !List.of(
                        "PROGRAMADA",
                        "ATENDIDA",
                        "CANCELADA"
                ).contains(cita.getEstado())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado no valido"
            );
        }

        // Una cita cancelada no ocupa horario
        if (cita.getEstado().equals("CANCELADA")) {
            return;
        }

        boolean ocupada = citaRepository.findAll()
                .stream()
                .anyMatch(existente ->
                        (idActual == null ||
                                !existente.getId().equals(idActual)) &&

                                existente.getFecha().equals(cita.getFecha()) &&
                                existente.getHora().equals(cita.getHora()) &&

                                !existente.getEstado().equals("CANCELADA")
                );

        if (ocupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una cita activa en esa fecha y hora"
            );
        }
    }
}
