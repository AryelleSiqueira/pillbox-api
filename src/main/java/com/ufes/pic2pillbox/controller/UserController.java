package com.ufes.pic2pillbox.controller;

import com.ufes.pic2pillbox.dto.UserDTO;
import com.ufes.pic2pillbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public UserDTO getUser() {
        return userService.getUser();
    }
}
