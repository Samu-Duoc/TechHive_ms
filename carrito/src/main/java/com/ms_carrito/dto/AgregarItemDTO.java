package com.ms_carrito.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
public class AgregarItemDTO {

    @NotBlank
    private String productoId;

    @NotNull
    @Min(1)
    private Integer cantidad;

    @NotNull
    private BigDecimal subtotal;
}
