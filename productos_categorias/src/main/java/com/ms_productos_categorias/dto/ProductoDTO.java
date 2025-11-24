package com.ms_productos_categorias.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    private Double precio;
    private String estado;
    private String categoria;
    private String disponibilidad;
    private String sku;
    
}
