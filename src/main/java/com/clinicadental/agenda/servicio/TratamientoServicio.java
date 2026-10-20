
package com.clinicadental.agenda.servicio;

import com.clinicadental.agenda.modelo.Tratamiento;
import com.clinicadental.agenda.repositorio.TratamientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TratamientoServicio {

    private final TratamientoRepository repositorio;

    public TratamientoServicio(TratamientoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Consultar todos los tratamientos
    public List<Tratamiento> listar() {
        return repositorio.findAll();
    }

    // Buscar un tratamiento por su ID
    public Tratamiento buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tratamiento no encontrado"
                ));
    }

    // Registrar un nuevo tratamiento
    public Tratamiento guardar(Tratamiento tratamiento) {
        validar(tratamiento);
        tratamiento.setId(null);
        return repositorio.save(tratamiento);
    }

    // Actualizar un tratamiento existente
    public Tratamiento actualizar(Long id, Tratamiento datos) {
        Tratamiento actual = buscarPorId(id);

        validar(datos);

        actual.setNombre(datos.getNombre());
        actual.setDescripcion(datos.getDescripcion());
        actual.setPrecio(datos.getPrecio());

        return repositorio.save(actual);
    }

    // Eliminar un tratamiento
    public void eliminar(Long id) {
        Tratamiento tratamiento = buscarPorId(id);
        repositorio.delete(tratamiento);
    }

    // Validaciones basicas
    private void validar(Tratamiento tratamiento) {
        if (tratamiento == null ||
                tratamiento.getNombre() == null ||
                tratamiento.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre del tratamiento es obligatorio"
            );
        }

        if (tratamiento.getPrecio() == null ||
                tratamiento.getPrecio().signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio debe ser mayor que cero"
            );
        }
    }
}