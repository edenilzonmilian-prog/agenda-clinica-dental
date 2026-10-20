
package com.clinicadental.agenda.repositorio;

import com.clinicadental.agenda.modelo.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TratamientoRepository
        extends JpaRepository<Tratamiento, Long> {

}
