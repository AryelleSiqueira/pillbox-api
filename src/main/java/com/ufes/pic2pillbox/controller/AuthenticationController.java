package com.ufes.pic2pillbox.controller;

import com.ufes.pic2pillbox.dto.auth.AuthenticationRequestDTO;
import com.ufes.pic2pillbox.dto.auth.AuthenticationResponseDTO;
import com.ufes.pic2pillbox.dto.auth.RegisterRequestDTO;
import com.ufes.pic2pillbox.exception.UserAlreadyExistsException;
import com.ufes.pic2pillbox.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return Map.of("error", ex.getMessage());
    }
}
