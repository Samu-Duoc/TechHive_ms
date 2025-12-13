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
    private String rut;

    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    @Size(min = 8, max = 10, message = "La contraseña debe tener entre 8 y 10 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[+*]).{8,10}$", message = "La contraseña debe contener al menos una mayúscula y un carácter especial (+ o *) y tener entre 8 y 10 caracteres")
    private String password;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;
}

