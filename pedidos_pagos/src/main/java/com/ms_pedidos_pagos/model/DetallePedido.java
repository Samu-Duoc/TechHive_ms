package com.ms_pedidos_pagos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedido")
@Data
public class DetallePedido {

    @Id
    @Column(name = "detalleP_id")
    private String detallePId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal;
}
