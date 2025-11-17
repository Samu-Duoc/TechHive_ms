package com.ms_productos_categorias.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms_productos_categorias.service.ProductoService;
import com.ms_productos_categorias.dto.ProductoDTO;
import com.ms_productos_categorias.dto.ActulizarStockDTO;

import java.util.List;
import jakarta.validation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor

public class ProductoController {

    private final ProductoService productoService;

    //Get de productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    //GET de producto por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    // Get de productos por categoría
    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.listarPorCategoria(nombre));
    }

    //Post de producto
    @PostMapping ResponseEntity<ProductoDTO> crear (@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok (productoService.crear(dto));
    }

    // Put de producto =  actuliza producto completo
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actulizarStock(@PathVariable Long id,@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    //Put de stock = actuliza solo el stock
    @PutMapping("/{id}/stock/")
    public ResponseEntity<ProductoDTO> actulizarStock(@PathVariable Long id, @Valid @RequestBody ActulizarStockDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto.getStock()));
    }

    //Delete de Producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // retorna 204 No Content

}
