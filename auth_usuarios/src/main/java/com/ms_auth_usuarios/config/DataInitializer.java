package com.ms_auth_usuarios.config;

import com.ms_auth_usuarios.model.Rol;
import com.ms_auth_usuarios.model.Usuario;
import com.ms_auth_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!usuarioRepository.existsByEmail("admin@techhive.cl")) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Principal")
                    .rut("11111111-1")
                    .email("admin@techhive.cl")
                    .password(passwordEncoder.encode("Admin1234+"))
                    .telefono("999999999")
                    .direccion("Sede Mi casa ")
                    .rol(Rol.ADMIN)
                    .estado("Activo")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(admin);
        }
    }
}

