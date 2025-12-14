package com.ms_pedidos_pagos.repository;

import com.ms_pedidos_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, String> {
	Optional<Pago> findFirstByPedidoId(String pedidoId);
}