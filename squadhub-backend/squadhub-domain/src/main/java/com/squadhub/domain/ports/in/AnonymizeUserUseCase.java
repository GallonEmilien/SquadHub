package com.squadhub.domain.ports.in;

import java.util.UUID;

public interface AnonymizeUserUseCase {
    void anonymizeUser(UUID userId);
}
