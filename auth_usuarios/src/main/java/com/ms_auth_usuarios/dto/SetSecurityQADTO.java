package com.ms_auth_usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetSecurityQADTO {

    @NotBlank private String pregunta;
    @NotBlank private String respuesta;
    @NotBlank private String currentPassword; // para asegurar que es el dueño
}
