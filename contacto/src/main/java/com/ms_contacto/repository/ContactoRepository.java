package com.ms_contacto.repository;

import com.ms_contacto.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, String> {

    List<Contacto> findByUsuarioIdOrderByFechaEnvioDesc(String usuarioId);
}