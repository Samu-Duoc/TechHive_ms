package com.ms_auth_usuarios.controller;

import com.ms_auth_usuarios.dto.CambiarPasswordDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.SetSecurityQADTO;
import com.ms_auth_usuarios.dto.UpdateProfileDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    // Actualizar usuario (si lo usas como "update completo")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Cambiar contraseña (solo password)
    @PutMapping("/{id}/password")
    public ResponseEntity<String> cambiarPassword(@PathVariable Long id,@Valid @RequestBody CambiarPasswordDTO dto) {
        usuarioService.cambiarPassword(id, dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    //Actualizar perfil SIN verificar currentPassword (solo datos del perfil)
    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@PathVariable Long id,@Valid @RequestBody UpdateProfileDTO dto) {
        // OJO: este método del service debe ser el "sin password"
        UsuarioDTO actualizado = usuarioService.actualizarPerfil(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Configurar pregunta/respuesta de seguridad
    @PutMapping("/{id}/security")
    public ResponseEntity<String> setSecurity(@PathVariable Long id,@Valid @RequestBody SetSecurityQADTO dto) {
        usuarioService.configurarPreguntaSeguridad(id, dto);
        return ResponseEntity.ok("Pregunta de seguridad actualizada");
    }
}
