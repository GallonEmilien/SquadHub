package com.squadhub.domain.ports.out;

import com.squadhub.domain.Expense;

import java.util.List;
import java.util.UUID;

public interface ExpenseStoragePort {
    List<Expense> getExpensesBySquadId(UUID squadId);
}
