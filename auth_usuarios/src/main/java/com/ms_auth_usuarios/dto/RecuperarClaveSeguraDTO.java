package com.ms_auth_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data

public class RecuperarClaveSeguraDTO {
    @Email @NotBlank private String email;
    @NotBlank private String respuesta;
    @NotBlank @Size(min=8) private String nuevaPassword;
}

