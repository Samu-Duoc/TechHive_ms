package com.ms_productos_categorias.repository;

import com.ms_productos_categorias.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos por el nombre de la categoría (categoria.nombre)
    List<Producto> findByCategoriaNombre(String nombre);

    //categoria = atributo de Producto
    //nombre =  atributo de Categoria

}
