package com.squadhub.domain.ports.out;

import java.util.Optional;

public interface TokenVerifierPort {
    record ExtUser(String email, String name, String avatar) {}
    Optional<ExtUser> verify(String externalToken);
}