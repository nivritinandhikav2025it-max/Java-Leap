package com.filmyjury.controller;

import com.filmyjury.model.AppUser;
import com.filmyjury.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo; this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body, HttpSession session) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");
        AppUser user = repo.findByUsername(username).orElse(null);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }

        session.setAttribute("USER", user.getUsername());
        session.setAttribute("ROLE", user.getRole());
        return ResponseEntity.ok(Map.of("username", user.getUsername(), "role", user.getRole()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object u = session.getAttribute("USER");
        if (u == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Not logged in"));
        return ResponseEntity.ok(Map.of("username", u, "role", session.getAttribute("ROLE")));
    }
}
