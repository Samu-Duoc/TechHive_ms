package com.ms_contacto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearContactoDTO {

    @NotBlank
    private String usuarioId;

    @NotBlank
    @Size(max = 100)
    private String asunto;

    @NotBlank
    @Size(max = 1000)
    private String mensaje;
}