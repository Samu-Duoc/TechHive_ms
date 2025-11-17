package com.ms_auth_usuarios.repository;

import com.ms_auth_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; //Sirve para manejar valores que pueden estar ausentes

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email); //Buscar usuario por email

    boolean existsByEmail(String email);  //Verificar si existe un usuario por email

    boolean existsByRut(String rut); //Verificar si existe un usuario por RUT



}
