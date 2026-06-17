package com.squadhub.domain.usecase;


import com.squadhub.domain.exceptions.InvalidTokenException;
import com.squadhub.domain.models.User;
import com.squadhub.domain.ports.out.TokenVerifierPort;
import com.squadhub.domain.ports.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
class AuthenticateUseCaseImpl implements AuthenticateUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final TokenVerifierPort tokenVerifierPort;

    AuthenticateUseCaseImpl(UserRepositoryPort userRepositoryPort, TokenVerifierPort tokenVerifierPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.tokenVerifierPort = tokenVerifierPort;
    }

    @Override
    public User authenticateWithGoogle(String idToken) {
        TokenVerifierPort.ExtUser extUser = tokenVerifierPort.verify(idToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid external identity token"));

        return userRepositoryPort.findByEmail(extUser.email())
                .map(user -> {
                    log.info("User logged in successfully. Internal userId: {}", user.id());
                    return user;
                })
                .orElseGet(() -> {
                    User newUser = userRepositoryPort.save(new User(UUID.randomUUID(), extUser.email(), extUser.name(), extUser.avatar()));
                    log.info("New user registered successfully. Internal userId: {}", newUser.id());
                    return newUser;
                });
    }
}