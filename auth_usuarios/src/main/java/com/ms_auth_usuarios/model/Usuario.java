package com.ms_auth_usuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {


    // Atributos de la entidad Usuario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 15)
    private String password;

    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(nullable = false, length = 11)
    private String telefono;


    //Enum para definir el rol = Administrador o Usuario (Cliente)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    // Fecha de registro del usuario
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    

    // Estado del usuario (Activo, Inactivo, Suspendido, etc.)
    @Column(nullable = false, length = 20)
    private String estado;
}
