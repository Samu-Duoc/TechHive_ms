package com.ms_pedidos_pagos.repository;

import com.ms_pedidos_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, String> {
}