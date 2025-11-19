package com.ms_contacto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacto")
@Data
public class Contacto {
    
    @Id
    @Column(name = "contacto_id")
    private String contactoId;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(nullable = false, length = 100)
    private String asunto;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(nullable = false)
    private String estado; // pendiente / respondido

}
