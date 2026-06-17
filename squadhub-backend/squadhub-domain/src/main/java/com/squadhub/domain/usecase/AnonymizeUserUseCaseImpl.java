package com.squadhub.domain.usecase;

import com.squadhub.domain.ports.in.AnonymizeUserUseCase;
import com.squadhub.domain.ports.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
class AnonymizeUserUseCaseImpl implements AnonymizeUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    AnonymizeUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public void anonymizeUser(UUID userId) {
        String anonymizedName = "Deleted User - " + UUID.randomUUID().toString().substring(0, 8);
        userRepositoryPort.anonymizeUser(userId, anonymizedName);
        log.info("User account successfully anonymized. Internal userId: {}", userId);
    }
}
