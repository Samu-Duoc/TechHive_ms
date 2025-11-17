package com.ms_auth_usuarios.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms_auth_usuarios.dto.LoginRequestDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UsuarioService usuarioService;

    //Registro de Usuario
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> resgistrar (@Valid @RequestBody RegistroUsuarioDTO dto) {
        UsuarioDTO creado = usuarioService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    //Inicio de Sesión
    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login (@Valid @RequestBody LoginRequestDTO dto) {
        UsuarioDTO usuario = usuarioService.login(dto);
        return ResponseEntity.ok(usuario);
    }

}
