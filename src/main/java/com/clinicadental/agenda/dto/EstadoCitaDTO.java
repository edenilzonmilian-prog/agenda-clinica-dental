package com.clinicadental.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EstadoCitaDTO {

    @NotBlank
    @Pattern(
            regexp = "ATENDIDA|CANCELADA",
            message = "El estado debe ser ATENDIDA o CANCELADA"
    )
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}