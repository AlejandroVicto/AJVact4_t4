package com.ale.democrud.controllers;

import com.ale.democrud.dtos.AuthResponseDTO;
import com.ale.democrud.dtos.LoginRequestDTO;
import com.ale.democrud.dtos.RegisterRequestDTO;
import com.ale.democrud.models.Usuario;
import com.ale.democrud.repository.UsuarioRepository;
import com.ale.democrud.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email ya está registrado"));
        }

        Usuario nuevoUsuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        usuarioRepository.save(nuevoUsuario);

        String token = jwtService.generateToken(nuevoUsuario.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDTO(token, nuevoUsuario.getNombre(), nuevoUsuario.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas (email o password incorrectos)"));
        }

        Usuario usuario = usuarioOpt.get();
        String token = jwtService.generateToken(usuario.getEmail());
        return ResponseEntity.ok(new AuthResponseDTO(token, usuario.getNombre(), usuario.getEmail()));
    }
}