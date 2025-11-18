package com.ms_carrito.repository;

import com.ms_carrito.model.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

}
