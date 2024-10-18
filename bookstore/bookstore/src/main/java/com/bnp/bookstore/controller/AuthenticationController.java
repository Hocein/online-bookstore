package com.bnp.bookstore.controller;

import com.bnp.bookstore.entity.User;
import com.bnp.bookstore.repository.UserRepository;
import com.bnp.bookstore.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.bnp.bookstore.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Check the type of the principal at runtime
        Object principal = authentication.getPrincipal();

        System.out.println("Principal class type: " + principal.getClass().getName());


        // Check if principal is an instance of CustomUserDetails
        if (principal instanceof CustomUserDetails customUserDetails) {

            // Extract the custom User entity from CustomUserDetails
            User user = customUserDetails.getUser();
            Long userId = user.getId();  // Get the user's ID

            // Generate JWT token with user ID
            String jwt = jwtTokenProvider.generateToken(user.getUsername(), userId);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already taken"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}


