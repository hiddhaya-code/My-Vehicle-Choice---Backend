package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.auth.AuthResponse;
import com.myvehiclechoice.dto.auth.LoginRequest;
import com.myvehiclechoice.dto.auth.RegisterRequest;
import com.myvehiclechoice.dto.auth.UserProfileDto;
import com.myvehiclechoice.exception.ResourceNotFoundException;
import com.myvehiclechoice.model.entity.User;
import com.myvehiclechoice.repository.UserRepository;
import com.myvehiclechoice.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmailIgnoreCase(req.getEmail())) {
            throw new IllegalArgumentException("An account with email " + req.getEmail() + " already exists.");
        }

        User user = new User();
        user.setEmail(req.getEmail().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName().trim());
        user.setPhone(req.getPhone());
        user.setCity(req.getCity() != null ? req.getCity() : "New Delhi");
        user.setRole("ROLE_USER");
        user.setIsActive(true);

        User saved = userRepository.save(user);
        String token = tokenProvider.generateToken(saved.getEmail(), saved.getRole());

        return new AuthResponse(token, saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole());
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase().trim(), req.getPassword())
        );

        User user = userRepository.findByEmailIgnoreCase(req.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

        return new UserProfileDto(user.getId(), user.getEmail(), user.getFullName(), user.getPhone(), user.getCity(), user.getRole());
    }
}
