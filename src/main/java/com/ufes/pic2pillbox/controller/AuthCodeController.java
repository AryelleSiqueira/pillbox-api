package com.ufes.pic2pillbox.controller;

import com.ufes.pic2pillbox.dto.auth.AuthCodeDTO;
import com.ufes.pic2pillbox.dto.auth.AuthenticationResponseDTO;
import com.ufes.pic2pillbox.exception.InvalidCodeException;
import com.ufes.pic2pillbox.exception.NoAssociatedUserException;
import com.ufes.pic2pillbox.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/code", produces = "application/json")
public class AuthCodeController {

    private final AuthCodeService authenticationService;


    @GetMapping("/get") // ESP8266 will call this endpoint to get a unique code
    public ResponseEntity<AuthCodeDTO> getCode() {
        return ResponseEntity.ok(authenticationService.getCode());
    }

    @GetMapping("/get/token/{code}") // ESP8266 will call this endpoint to get auth token
    public ResponseEntity<AuthenticationResponseDTO> getTokenByCode(@PathVariable int code) {
        return ResponseEntity.ok(authenticationService.getTokenByCode(code));
    }

    @PostMapping("/associate") // Mobile app will call this endpoint to associate user with code
    @ResponseStatus(HttpStatus.OK)
    public void associateUser(@RequestBody AuthCodeDTO authCode) {
        authenticationService.associateUser(authCode);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCodeException.class)
    public Map<String, Object> handleInvalidCodeException(InvalidCodeException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAssociatedUserException.class)
    public Map<String, Object> handleNoAssociatedUserException(NoAssociatedUserException ex) {
        return Map.of("error", ex.getMessage());
    }
}
