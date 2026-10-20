
package com.clinicadental.agenda.repositorio;

import com.clinicadental.agenda.modelo.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository
        extends JpaRepository<Cita, Long> {

}