package com.ms_productos_categorias.repository;

import com.ms_productos_categorias.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria_Nombre(Long categoriaId);

    // categoria es = atrubuto de Producto
    // nombre es = atributo de Categoria

}
