package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Cita;
import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.repositorio.CitaRepository;
import com.clinicadental.agenda.repositorio.TratamientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CitaServicioTest {

    private CitaRepository citaRepository;
    private TratamientoRepository tratamientoRepository;
    private CitaServicio servicio;

    @BeforeEach
    void preparar() {
        citaRepository = mock(CitaRepository.class);
        tratamientoRepository = mock(TratamientoRepository.class);
        servicio = new CitaServicio(
                citaRepository,
                tratamientoRepository
        );
    }

    private Cita crearCita() {
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(4L);
        tratamiento.setPrecio(new BigDecimal("600.00"));

        Cita cita = new Cita();
        cita.setPaciente("Paciente de prueba");
        cita.setFecha(LocalDate.now(
                ZoneId.of("America/Guatemala")
        ).plusDays(3));
        cita.setHora(LocalTime.of(10, 0));
        cita.setTratamiento(tratamiento);
        cita.setEstado("PROGRAMADA");

        return cita;
    }

    @Test
    void rechazarHorarioOcupado() {
        Cita cita = crearCita();

        when(citaRepository.existsByFechaAndHoraAndEstadoNot(
                any(LocalDate.class),
                any(LocalTime.class),
                eq("CANCELADA")
        )).thenReturn(true);

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> servicio.guardar(cita)
        );

        assertEquals(409, error.getStatusCode().value());
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void rechazarFechaPasada() {
        Cita cita = crearCita();
        cita.setFecha(LocalDate.now(
                ZoneId.of("America/Guatemala")
        ).minusDays(1));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> servicio.guardar(cita)
        );

        assertEquals(400, error.getStatusCode().value());
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void guardarPrecioAcordado() {
        Cita cita = crearCita();

        when(tratamientoRepository.findById(4L))
                .thenReturn(Optional.of(cita.getTratamiento()));

        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Cita guardada = servicio.guardar(cita);

        assertEquals(
                new BigDecimal("600.00"),
                guardada.getPrecioAcordado()
        );
    }

    @Test
    void conservarPrecioHistorico() {
        Cita actual = crearCita();
        actual.setId(3L);
        actual.setPrecioAcordado(new BigDecimal("600.00"));

        Cita datos = crearCita();

        Tratamiento tratamientoActualizado = new Tratamiento();
        tratamientoActualizado.setId(4L);
        tratamientoActualizado.setPrecio(new BigDecimal("750.00"));

        when(citaRepository.findById(3L))
                .thenReturn(Optional.of(actual));

        when(tratamientoRepository.findById(4L))
                .thenReturn(Optional.of(tratamientoActualizado));

        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Cita resultado = servicio.actualizar(3L, datos);

        assertEquals(
                new BigDecimal("600.00"),
                resultado.getPrecioAcordado()
        );
    }

    @Test
    void permitirMarcarCitaAtendida() {
        Cita cita = crearCita();
        cita.setId(1L);

        when(citaRepository.findById(1L))
                .thenReturn(Optional.of(cita));

        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Cita resultado = servicio.cambiarEstado(1L, "ATENDIDA");

        assertEquals("ATENDIDA", resultado.getEstado());
    }

    @Test
    void permitirCancelarCita() {
        Cita cita = crearCita();
        cita.setId(2L);

        when(citaRepository.findById(2L))
                .thenReturn(Optional.of(cita));

        when(citaRepository.save(any(Cita.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Cita resultado = servicio.cambiarEstado(2L, "CANCELADA");

        assertEquals("CANCELADA", resultado.getEstado());
    }

    @Test
    void impedirModificarCitaAtendida() {
        Cita cita = crearCita();
        cita.setId(5L);
        cita.setEstado("ATENDIDA");

        when(citaRepository.findById(5L))
                .thenReturn(Optional.of(cita));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> servicio.actualizar(5L, crearCita())
        );

        assertEquals(409, error.getStatusCode().value());
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    void impedirCambiarEstadoDeCitaCancelada() {
        Cita cita = crearCita();
        cita.setId(6L);
        cita.setEstado("CANCELADA");

        when(citaRepository.findById(6L))
                .thenReturn(Optional.of(cita));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> servicio.cambiarEstado(6L, "ATENDIDA")
        );

        assertEquals(409, error.getStatusCode().value());
        verify(citaRepository, never()).save(any(Cita.class));
    }
}