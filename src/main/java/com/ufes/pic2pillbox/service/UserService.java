package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.UserDTO;
import com.ufes.pic2pillbox.model.Code;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.CodeRepository;
import com.ufes.pic2pillbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final CodeRepository codeRepository;

    public UserDTO getUser() {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        final User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        final Code pillboxCode = codeRepository.findByUserId(userId).orElse(null);

        String codeString = null;

        if (pillboxCode != null) {
            codeString = pillboxCode.getCode().toString();
            codeString = codeString.charAt(0) +
                    "*".repeat(Math.max(0, codeString.length() - 2)) +
                    codeString.charAt(codeString.length() - 1);
        }

        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .associatedPillbox(codeString)
                .build();
    }
}
