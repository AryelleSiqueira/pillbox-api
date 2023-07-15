package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.UserDTO;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO getUser() {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        final User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
