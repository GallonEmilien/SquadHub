package com.squadhub.domain.models;

import java.util.UUID;

public record User(
        UUID id,
        String email,
        String name,
        String avatar
) {}