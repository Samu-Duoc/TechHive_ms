package com.ms_auth_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroUsuarioDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String rut;

    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    @Size(min = 10, message = "La contraseña debe tener al menos 10 caracteres")
    private String password;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;
}

