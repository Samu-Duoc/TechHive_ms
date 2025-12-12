package com.ms_carrito.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarItemDTO {

    @NotNull
    @Min(1)
    private Integer cantidad;

    @NotNull
    @Min(0)
    private BigDecimal subtotal; 

}
