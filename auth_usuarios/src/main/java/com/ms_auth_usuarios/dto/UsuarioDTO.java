package com.ms_auth_usuarios.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rut;
    private String telefono;
    private String direccion;
    private String rol;
    private String estado;
    private LocalDateTime fechaRegistro;

}
