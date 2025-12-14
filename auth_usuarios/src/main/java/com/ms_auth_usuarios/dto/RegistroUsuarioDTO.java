package com.ms_auth_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class RegistroUsuarioDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Pattern(regexp = "^\\d{7,8}-?[0-9K]$", message = "El RUT debe tener formato válido (ej: 12345678-K o 12345678K)")
    private String rut;

    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?]).{8,}$", message = "La contraseña debe contener mayúscula, minúscula, número y carácter especial")
    private String password;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;
}

