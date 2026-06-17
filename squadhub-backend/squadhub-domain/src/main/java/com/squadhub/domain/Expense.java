package com.squadhub.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Expense(
        UUID id,
        UUID squadId,
        UUID paidByMemberId,
        BigDecimal amount,
        String description,
        LocalDateTime date,
        List<ExpensePart> parts
) {
    public record ExpensePart(
            UUID memberId,
            BigDecimal weight,
            boolean excluded
    ) {}
}
