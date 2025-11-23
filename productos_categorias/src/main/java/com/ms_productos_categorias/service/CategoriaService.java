package com.ms_productos_categorias.service;

import com.ms_productos_categorias.dto.CategoriaDTO;
import com.ms_productos_categorias.model.Categoria;
import com.ms_productos_categorias.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(cat -> CategoriaDTO.builder()
                        .id(cat.getId())
                        .nombre(cat.getNombre())
                        .build()
                )
                .toList();
    }

    public CategoriaDTO crearCategoria(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("La categoría ya existe");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .build();

        Categoria guardada = categoriaRepository.save(categoria);

        return CategoriaDTO.builder()
                .id(guardada.getId())
                .nombre(guardada.getNombre())
                .build();
    }
}

