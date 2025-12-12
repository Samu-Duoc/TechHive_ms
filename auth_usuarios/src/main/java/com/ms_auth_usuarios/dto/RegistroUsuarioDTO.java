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
    @Size(min = 6, max = 8, message = "La contraseña debe tener entre 6 y 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[+*]).{6,8}$",message = "La contraseña debe contener al menos una mayúscula y un carácter especial (+ o *)")
    private String password;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;
}

