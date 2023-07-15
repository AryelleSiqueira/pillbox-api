package com.ufes.pic2pillbox.service;

import com.ufes.pic2pillbox.dto.auth.AuthCodeDTO;
import com.ufes.pic2pillbox.dto.auth.AuthenticationResponseDTO;
import com.ufes.pic2pillbox.exception.InvalidCodeException;
import com.ufes.pic2pillbox.exception.NoAssociatedUserException;
import com.ufes.pic2pillbox.model.Code;
import com.ufes.pic2pillbox.model.User;
import com.ufes.pic2pillbox.repository.CodeRepository;
import com.ufes.pic2pillbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final UserRepository userRepository;

    private final CodeRepository codeRepository;

    private final JwtService jwtService;

    private static final Random random = new Random();


    public AuthCodeDTO generateCode() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        int hash = Math.abs(Objects.hash(System.currentTimeMillis(), random.nextInt(), sessionId));
        hash = hash > 9999999 ? hash / 1000 : hash;

        if (codeRepository.existsById(hash)) {
            return generateCode();
        }
        final Code code = Code.builder()
                .code(hash)
                .build();

        codeRepository.save(code);

        return AuthCodeDTO.builder()
                .code(code.getCode())
                .build();
    }

    public AuthenticationResponseDTO getTokenByCode(AuthCodeDTO code) {
        final Code codeEntity = codeRepository.findById(code.getCode())
                .orElseThrow(() -> new InvalidCodeException("No pillbox registered with this code."));

        final User user = codeEntity.getUser();

        if (user == null) {
            throw new NoAssociatedUserException("No associated user.");
        }
        final String jwt = jwtService.generateToken(Map.of("code", code), user);

        return AuthenticationResponseDTO.builder()
                .token(jwt)
                .build();
    }

    public void associateUser(AuthCodeDTO code) {
        final Code codeEntity = codeRepository.findById(code.getCode())
                .orElseThrow(() -> new InvalidCodeException("No pillbox registered with this code."));

        if (codeEntity.getUser() != null) {
            throw new InvalidCodeException("Code already associated with a user.");
        }
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userRepository.findById(userId).ifPresent(codeEntity::setUser);
        codeRepository.save(codeEntity);
    }

    public void disassociateUser() {
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        final Code codeEntity = codeRepository.findByUserId(userId)
                .orElseThrow(() -> new NoAssociatedUserException("User not associated with a pillbox."));

        codeEntity.setUser(null);
        codeRepository.save(codeEntity);
    }
}
