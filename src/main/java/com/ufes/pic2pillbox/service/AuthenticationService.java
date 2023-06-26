package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.auth.AuthenticationRequestDTO;
import com.ufes.pic2pillbox.dto.auth.AuthenticationResponseDTO;
import com.ufes.pic2pillbox.dto.auth.RegisterRequestDTO;
import com.ufes.pic2pillbox.exception.UserAlreadyExistsException;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use.");
        }
        final User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        return AuthenticationResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return AuthenticationResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
