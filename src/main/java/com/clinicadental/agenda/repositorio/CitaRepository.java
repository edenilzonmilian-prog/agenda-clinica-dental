package com.clinicadental.agenda.repositorio;

import com.clinicadental.agenda.modelo.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFecha(LocalDate fecha);

    List<Cita> findByTratamiento_Id(Long tratamientoId);

    List<Cita> findByEstadoOrderByFechaAscHoraAsc(String estado);

    @Query("SELECT SUM(c.tratamiento.precio) FROM Cita c WHERE c.estado = 'PROGRAMADA'")
    BigDecimal calcularTotalEstimadoProgramadas();

    boolean existsByFechaAndHoraAndEstadoNot(
            LocalDate fecha,
            LocalTime hora,
            String estado
    );

    boolean existsByFechaAndHoraAndEstadoNotAndIdNot(
            LocalDate fecha,
            LocalTime hora,
            String estado,
            Long id
    );
}