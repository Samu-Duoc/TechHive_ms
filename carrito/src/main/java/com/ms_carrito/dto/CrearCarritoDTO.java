package com.ms_carrito.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CrearCarritoDTO {
    @NotNull
    private Long usuarioId;
}
