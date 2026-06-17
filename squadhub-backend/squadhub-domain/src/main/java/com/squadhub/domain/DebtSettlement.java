package com.squadhub.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record DebtSettlement(
        UUID fromMemberId,
        UUID toMemberId,
        BigDecimal amount
) {}
