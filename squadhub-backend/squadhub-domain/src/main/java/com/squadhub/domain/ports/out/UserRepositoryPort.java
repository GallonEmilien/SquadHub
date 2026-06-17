package com.squadhub.domain.ports.out;

import com.squadhub.domain.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    void anonymizeUser(UUID userId, String anonymizedName);
    Optional<User> findByEmail(String email);
    User save(User user);
}
