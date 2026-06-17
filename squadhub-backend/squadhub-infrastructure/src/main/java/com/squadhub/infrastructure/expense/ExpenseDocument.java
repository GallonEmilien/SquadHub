package com.squadhub.infrastructure.expense;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "expenses")
@CompoundIndex(name = "squadId_1", def = "{'squadId': 1}")
public record ExpenseDocument(
        @Id
        UUID id,
        UUID squadId,
        UUID paidByMemberId,
        BigDecimal amount,
        String description,
        LocalDateTime date,
        List<ExpensePartDocument> parts
) {
    public record ExpensePartDocument(
            UUID memberId,
            BigDecimal weight,
            boolean excluded
    ) {}
}
