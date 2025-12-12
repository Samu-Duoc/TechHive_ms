package com.ms_productos_categorias.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private BigDecimal precio;
    private String estado;
    private String categoria;
    private String disponibilidad;
    private String sku;
    private String imagenBase64; //Para mandar y recibir la imagen desde el front-end
}
