package com.ms_auth_usuarios.service;

import org.springframework.stereotype.Service;

import com.ms_auth_usuarios.dto.LoginRequestDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.model.Rol;
import com.ms_auth_usuarios.model.Usuario;
import com.ms_auth_usuarios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    
    // Metodo para registrar un nuevo usuario
    public UsuarioDTO registrar (RegistroUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (usuarioRepository.existsByRut(dto.getRut())){
            throw new IllegalArgumentException("El RUT ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .rut(dto.getRut())
                .email(dto.getEmail())
                .password(dto.getPassword()) // En un entorno real, la contraseña debe ser hasheada
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .rol(Rol.CLIENTE)
                .estado("Activo")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        Usuario guardado = usuarioRepository.save(usuario);

        return toDTO(guardado);
    }

    //Metodo para Iniciar sesión de un usuario
    public UsuarioDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("email o contraseña incorrectos"));
        
        if (!usuario.getPassword().equals(dto.getPassword())) {
            throw new IllegalArgumentException("email o contraseña incorrectos");
        }

        return toDTO(usuario);
    }

    // Metodo para convertir Usuario a UsuarioDTO
    private UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rut(usuario.getRut())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .rol(usuario.getRol().name())
                .estado(usuario.getEstado())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }


    //Metodo para obtener Usuario por ID
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toDTO(usuario);
    }

    //Actulizar un usuario por ID
    public UsuarioDTO actualizarUsuario(Long id, RegistroUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //Compos basicos para la actualizar
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRut(dto.getRut());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());

        Usuario actualizado = usuarioRepository.save(usuario);
        return toDTO(actualizado);
    }
}
