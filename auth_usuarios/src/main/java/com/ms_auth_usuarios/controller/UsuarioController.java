package com.ms_auth_usuarios.controller;

import com.ms_auth_usuarios.dto.CambiarPasswordDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.UpdateProfileDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;

    //Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId (@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<java.util.List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    //Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar (@PathVariable Long id, @Valid @RequestBody RegistroUsuarioDTO dto){

        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);

        return ResponseEntity.ok(actualizado);
    
    }

    //cambiar contraseña
    @PutMapping("/{id}/password")
    public ResponseEntity<String> cambiarPassword(@PathVariable Long id, @Valid @RequestBody CambiarPasswordDTO dto) {
        usuarioService.cambiarPassword(id, dto);
        
        return ResponseEntity.ok("Contraseña actualizada correctamente");

    }
    //Actualizar perfil con verificación de contraseña actual
    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@PathVariable Long id,
                                                @Valid @RequestBody UpdateProfileDTO dto) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuarioConPassword(id, dto);
        return ResponseEntity.ok(actualizado);
    }

}

