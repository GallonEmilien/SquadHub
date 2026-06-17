package com.squadhub.domain.usecase;

import com.squadhub.domain.models.User;

public interface AuthenticateUseCase {
    User authenticateWithGoogle(String idToken);
}