package com.ms_pedidos_pagos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data

public class Pedido {

    @Id
    @Column(name = "pedido_id")
    private String pedidoId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "direccion_id", nullable = false)
    private String direccionId;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String estado; // EJ: "PAGADO"

    @Column(nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

}
