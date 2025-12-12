package com.ms_carrito.service;

import com.ms_carrito.dto.*;
import com.ms_carrito.model.Carrito;
import com.ms_carrito.model.DetalleCarrito;
import com.ms_carrito.repository.CarritoRepository;
import com.ms_carrito.repository.DetalleCarritoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;


@Service
@RequiredArgsConstructor

public class CarritoService {

        private final CarritoRepository carritoRepository;
        private final DetalleCarritoRepository detalleCarritoRepository;

    //Carrito para un usuario (Si ya tiene carrito activo, retorna ese)
        public CarritoDTO crearCarrito(CrearCarritoDTO dto) {

        Carrito carrito = carritoRepository.findByUsuarioId(dto.getUsuarioId())
                .orElseGet(() -> {
                        Carrito nuevo = Carrito.builder()
                        .usuarioId(dto.getUsuarioId())
                        .fechaCreacion(LocalDateTime.now())
                        .build();
                        return carritoRepository.save(nuevo);
                });

        return toDTO(carrito);
        }

         // Obtener carrito por usuario
        public CarritoDTO obtenerPorUsuario(Long usuarioId) {

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene carrito"));

        return toDTO(carrito);
        }

        //Agregar producto al carrito
        public CarritoDTO agregarItem(Long carritoId, AgregarItemDTO dto) {

        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
        
        DetalleCarrito detalle = DetalleCarrito.builder()
                .carrito(carrito)
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .subtotal(dto.getSubtotal())
                .build();
        
        detalleCarritoRepository.save(detalle);

        //Recargar  carrito con sus items
        Carrito actualizado = carritoRepository.findById(carritoId).orElseThrow();
        return toDTO(actualizado);
        }

        //Actulizar item del carrito
        public CarritoDTO actualizarItem(Long carritoId, Long detalleId, ActualizarItemDTO dto) {

        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        DetalleCarrito detalle = detalleCarritoRepository.findById(detalleId)
                .orElseThrow(() -> new IllegalArgumentException("Item del carrito no encontrado"));

        if (!detalle.getCarrito().getId().equals(carrito.getId())) {
                throw new IllegalArgumentException("El item no pertenece a este carrito");
        }

        detalle.setCantidad(dto.getCantidad());
        detalle.setSubtotal(dto.getSubtotal());
        detalleCarritoRepository.save(detalle);

        Carrito actualizado = carritoRepository.findById(carritoId).orElseThrow();

        return toDTO(actualizado);
        }

        // Eliminar ítem
        public CarritoDTO eliminarItem(Long carritoId, Long detalleId) {

        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        DetalleCarrito detalle = detalleCarritoRepository.findById(detalleId)
                .orElseThrow(() -> new IllegalArgumentException("Item del carrito no encontrado"));

        if (!detalle.getCarrito().getId().equals(carrito.getId())) {
                throw new IllegalArgumentException("El item no pertenece a este carrito");
        }

        detalleCarritoRepository.delete(detalle);

        Carrito actualizado = carritoRepository.findById(carritoId).orElseThrow();
        return toDTO(actualizado);
        }

        // Vaciar carrito (eliminar todos los ítems)
        public CarritoDTO vaciarCarrito(Long carritoId) {

        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.getDetalles().clear();
        carritoRepository.save(carrito);

        Carrito actualizado = carritoRepository.findById(carritoId).orElseThrow();
        return toDTO(actualizado);
        }

        // Eliminar carrito completo
        public void eliminarCarrito(Long carritoId) {
        if (!carritoRepository.existsById(carritoId)) {
                throw new IllegalArgumentException("Carrito no encontrado");
        }
        carritoRepository.deleteById(carritoId);
        }

        // Helpers: convertir a DTO y calcular total
        private CarritoDTO toDTO(Carrito carrito) {

        List<ItemCarritoDTO> items = carrito.getDetalles()
                .stream()
                .map(d -> ItemCarritoDTO.builder()
                        .id(d.getId())
                        .productoId(d.getProductoId())
                        .cantidad(d.getCantidad())
                        .subtotal(d.getSubtotal())
                        .build()
                ).toList();

        BigDecimal total = items.stream()
                .map(ItemCarritoDTO::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CarritoDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .fechaCreacion(carrito.getFechaCreacion())
                .total(total)
                .items(items)
                .build();
        }

}
