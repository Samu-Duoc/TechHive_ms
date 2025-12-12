package com.ms_productos_categorias.controller;

import com.ms_productos_categorias.dto.ActulizarStockDTO;
import com.ms_productos_categorias.dto.ProductoDTO;
import com.ms_productos_categorias.service.ProductoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.listarPorCategoria(nombre));
    }


    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.crear(dto));
    }

    @PostMapping(
            value = "/{id}/imagen",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> subirImagen(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {

        productoService.actualizarImagen(id, archivo);
        return ResponseEntity.ok("Imagen subida correctamente");
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDTO dto) {

        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductoDTO> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody ActulizarStockDTO dto) {

        return ResponseEntity.ok(productoService.actualizar(id, dto.getStock()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
