package com.ms_contacto.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactoDTO {

    private String contactoId;
    private String usuarioId;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estado;
}
