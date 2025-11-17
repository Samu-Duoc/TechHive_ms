package com.ms_productos_categorias.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ActulizarStockDTO {

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

}
