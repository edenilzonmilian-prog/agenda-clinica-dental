package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.repositorio.CitaRepository;
import com.clinicadental.agenda.repositorio.TratamientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TratamientoServicio {

    private final TratamientoRepository repositorio;
    private final CitaRepository citaRepository;

    public TratamientoServicio(
            TratamientoRepository repositorio,
            CitaRepository citaRepository) {
        this.repositorio = repositorio;
        this.citaRepository = citaRepository;
    }

    public List<Tratamiento> listar() {
        return repositorio.findAll();
    }

    public Tratamiento buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tratamiento no encontrado"
                ));
    }

    @Transactional
    public Tratamiento guardar(Tratamiento tratamiento) {
        validar(tratamiento);

        tratamiento.setId(null);
        tratamiento.setNombre(tratamiento.getNombre().trim());

        return repositorio.save(tratamiento);
    }

    @Transactional
    public Tratamiento actualizar(Long id, Tratamiento datos) {
        Tratamiento actual = buscarPorId(id);

        validar(datos);

        actual.setNombre(datos.getNombre().trim());
        actual.setDescripcion(datos.getDescripcion());
        actual.setPrecio(datos.getPrecio());

        return repositorio.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        Tratamiento tratamiento = buscarPorId(id);

        if (citaRepository.existsByTratamiento_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un tratamiento que tiene citas registradas"
            );
        }

        repositorio.delete(tratamiento);
    }

    private void validar(Tratamiento tratamiento) {
        if (tratamiento == null
                || tratamiento.getNombre() == null
                || tratamiento.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del tratamiento es obligatorio"
            );
        }

        if (tratamiento.getPrecio() == null
                || tratamiento.getPrecio().signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio debe ser mayor que cero"
            );
        }
    }
}