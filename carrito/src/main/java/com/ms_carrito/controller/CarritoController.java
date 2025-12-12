package com.ms_carrito.controller;

import com.ms_carrito.dto.*;
import com.ms_carrito.service.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class CarritoController {

    private final CarritoService carritoService;

    // Crear carrito para un usuario (si ya tiene, devuelve el mismo)
    @PostMapping
    public ResponseEntity<CarritoDTO> crearCarrito(@Valid @RequestBody CrearCarritoDTO dto) {
        return ResponseEntity.ok(carritoService.crearCarrito(dto));
    }

    // Obtener carrito por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoDTO> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerPorUsuario(usuarioId));
    }

    // Agregar ítem al carrito
    @PostMapping("/{carritoId}/items")
    public ResponseEntity<CarritoDTO> agregarItem(
            @PathVariable Long carritoId,
            @Valid @RequestBody AgregarItemDTO dto) {

        return ResponseEntity.ok(carritoService.agregarItem(carritoId, dto));
    }

    // Actualizar ítem del carrito
    @PutMapping("/{carritoId}/items/{detalleId}")
    public ResponseEntity<CarritoDTO> actualizarItem(
            @PathVariable Long carritoId,
            @PathVariable Long detalleId,
            @Valid @RequestBody ActualizarItemDTO dto) {

        return ResponseEntity.ok(carritoService.actualizarItem(carritoId, detalleId, dto));
    }

    // Eliminar ítem del carrito
    @DeleteMapping("/{carritoId}/items/{detalleId}")
    public ResponseEntity<CarritoDTO> eliminarItem(
            @PathVariable Long carritoId,
            @PathVariable Long detalleId) {

        return ResponseEntity.ok(carritoService.eliminarItem(carritoId, detalleId));
    }

    // Vaciar carrito (eliminar todos los ítems)
    @DeleteMapping("/{carritoId}/items")
    public ResponseEntity<CarritoDTO> vaciarCarrito(@PathVariable Long carritoId) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(carritoId));
    }

    // Eliminar carrito completamente
    @DeleteMapping("/{carritoId}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long carritoId) {
        carritoService.eliminarCarrito(carritoId);
        return ResponseEntity.noContent().build();
    }
}
