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

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final UserRepository userRepository;

    private final CodeRepository codeRepository;

    private final JwtService jwtService;

    private static final Random random = new Random();


    public AuthCodeDTO getCode() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        int hash = Math.abs(Objects.hash(System.currentTimeMillis(), random.nextInt(), sessionId));
        hash = hash > 9999999 ? hash / 1000 : hash;

        if (codeRepository.existsById(hash)) {
            return getCode();
        }
        final Code code = Code.builder()
                .code(hash)
                .expiresIn(System.currentTimeMillis() + 15 * 60 * 1000)
                .build();

        codeRepository.save(code);

        return AuthCodeDTO.builder()
                .code(code.getCode())
                .build();
    }

    public AuthenticationResponseDTO getTokenByCode(int code) {
        final Code codeEntity = codeRepository.findById(code)
                .orElseThrow(() -> new InvalidCodeException("Invalid code."));

        if (codeEntity.getExpiresIn() < System.currentTimeMillis()) {
            codeRepository.delete(codeEntity);
            throw new InvalidCodeException("Code expired.");
        }
        if (codeEntity.getUser() == null) {
            throw new NoAssociatedUserException("No associated user.");
        }
        final User user = codeEntity.getUser();
        user.setPillboxAssociated(true);
        userRepository.save(user);

        final String jwt = jwtService.generateToken(user);

        codeRepository.delete(codeEntity);

        return AuthenticationResponseDTO.builder()
                .token(jwt)
                .build();
    }

    public void associateUser(int code) {
        final Code codeEntity = codeRepository.findById(code)
                .orElseThrow(() -> new InvalidCodeException("Invalid code."));

        if (codeEntity.getExpiresIn() < System.currentTimeMillis()) {
            codeRepository.delete(codeEntity);
            throw new InvalidCodeException("Code expired.");
        }
        if (codeEntity.getUser() != null) {
            throw new InvalidCodeException("Code already associated.");
        }
        final int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userRepository.findById(userId).ifPresent(codeEntity::setUser);
        codeRepository.save(codeEntity);
    }
}
