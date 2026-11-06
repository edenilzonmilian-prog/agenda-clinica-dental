package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.repositorio.CitaRepository;
import com.clinicadental.agenda.repositorio.TratamientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    public List<Cita> listarPorTratamiento(Long tratamientoId) {
        buscarTratamiento(tratamientoId);
        return citaRepository.findByTratamiento_Id(tratamientoId);
    }

    public List<Cita> listarPendientes() {
        return citaRepository.findByEstadoOrderByFechaAscHoraAsc("PROGRAMADA");
    }

    public BigDecimal calcularTotalEstimadoProgramadas() {
        BigDecimal total = citaRepository.calcularTotalEstimadoProgramadas();
        return total == null ? BigDecimal.ZERO : total;
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cita no encontrada"
                ));
    }

    @Transactional
    public Cita guardar(Cita cita) {
        validarDatos(cita);
        validarFechaFutura(cita);
        validarEstadoProgramado(cita.getEstado());
        validarHorario(cita, null);

        Tratamiento tratamiento = buscarTratamiento(
                cita.getTratamiento().getId()
        );

        cita.setId(null);
        cita.setPaciente(cita.getPaciente().trim());
        cita.setTratamiento(tratamiento);
        cita.setPrecioAcordado(tratamiento.getPrecio());

        return citaRepository.save(cita);
    }

    @Transactional
    public Cita actualizar(Long id, Cita datos) {
        Cita actual = buscarPorId(id);

        validarCitaModificable(actual);
        validarDatos(datos);
        validarFechaFutura(datos);
        validarEstadoProgramado(datos.getEstado());
        validarHorario(datos, id);

        Tratamiento tratamiento = buscarTratamiento(
                datos.getTratamiento().getId()
        );

        if (!actual.getTratamiento().getId().equals(tratamiento.getId())
                || actual.getPrecioAcordado() == null) {
            actual.setPrecioAcordado(tratamiento.getPrecio());
        }

        actual.setPaciente(datos.getPaciente().trim());
        actual.setFecha(datos.getFecha());
        actual.setHora(datos.getHora());
        actual.setTratamiento(tratamiento);
        actual.setObservaciones(datos.getObservaciones());

        return citaRepository.save(actual);
    }

    @Transactional
    public Cita cambiarEstado(Long id, String nuevoEstado) {
        Cita cita = buscarPorId(id);

        validarCitaModificable(cita);

        if (!"ATENDIDA".equals(nuevoEstado)
                && !"CANCELADA".equals(nuevoEstado)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado no valido"
            );
        }

        cita.setEstado(nuevoEstado);

        return citaRepository.save(cita);
    }

    @Transactional
    public void eliminar(Long id) {
        Cita cita = buscarPorId(id);

        validarCitaModificable(cita);

        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }

    private Tratamiento buscarTratamiento(Long id) {
        return tratamientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tratamiento no encontrado"
                ));
    }

    private void validarCitaModificable(Cita cita) {
        if (!"PROGRAMADA".equals(cita.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se permiten cambios en citas atendidas o canceladas"
            );
        }
    }

    private void validarEstadoProgramado(String estado) {
        if (!"PROGRAMADA".equals(estado)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Para registrar o actualizar una cita, el estado debe ser PROGRAMADA"
            );
        }
    }

    private void validarDatos(Cita cita) {
        if (cita == null
                || cita.getPaciente() == null
                || cita.getPaciente().isBlank()
                || cita.getFecha() == null
                || cita.getHora() == null
                || cita.getTratamiento() == null
                || cita.getTratamiento().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faltan datos obligatorios"
            );
        }
    }

    private void validarFechaFutura(Cita cita) {
        LocalDateTime fechaHoraCita = LocalDateTime.of(
                cita.getFecha(),
                cita.getHora()
        );

        LocalDateTime ahora = LocalDateTime.now(
                ZoneId.of("America/Guatemala")
        );

        if (!fechaHoraCita.isAfter(ahora)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha y hora de la cita deben ser posteriores al momento actual"
            );
        }
    }

    private void validarHorario(Cita cita, Long idActual) {
        boolean ocupada;

        if (idActual == null) {
            ocupada = citaRepository.existsByFechaAndHoraAndEstadoNot(
                    cita.getFecha(),
                    cita.getHora(),
                    "CANCELADA"
            );
        } else {
            ocupada = citaRepository.existsByFechaAndHoraAndEstadoNotAndIdNot(
                    cita.getFecha(),
                    cita.getHora(),
                    "CANCELADA",
                    idActual
            );
        }

        if (ocupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una cita activa en esa fecha y hora"
            );
        }
    }
}