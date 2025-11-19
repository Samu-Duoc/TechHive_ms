package com.ms_pedidos_pagos.repository;

import com.ms_pedidos_pagos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, String> {
}