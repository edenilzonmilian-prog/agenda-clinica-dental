
package com.clinicadental.agenda.repositorio;

import com.clinicadental.agenda.modelo.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository
        extends JpaRepository<Cita, Long> {

    // Consultar citas por fecha
    List<Cita> findByFecha(LocalDate fecha);

    // Consultar citas por tratamiento
    List<Cita> findByTratamiento_Id(Long tratamientoId);

}