package com.squadhub.domain.usecase;

import com.squadhub.domain.DebtSettlement;
import com.squadhub.domain.Expense;
import com.squadhub.domain.ports.out.ExpenseStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettleDebtsUseCaseImplTest {

    @Mock
    private ExpenseStoragePort expenseStoragePort;

    @InjectMocks
    private SettleDebtsUseCaseImpl settleDebtsUseCase;

    @Test
    void settleDebts_should_balance_asymmetric_weights() {
        UUID squadId = UUID.randomUUID();
        UUID alice = UUID.randomUUID();
        UUID bob = UUID.randomUUID();
        UUID charlie = UUID.randomUUID();
        UUID david = UUID.randomUUID();

        List<Expense.ExpensePart> parts = List.of(
                new Expense.ExpensePart(alice, BigDecimal.ONE, false),
                new Expense.ExpensePart(bob, BigDecimal.valueOf(0.5), false),
                new Expense.ExpensePart(charlie, BigDecimal.ONE, false),
                new Expense.ExpensePart(david, BigDecimal.ZERO, true)
        );

        Expense expense = new Expense(
                UUID.randomUUID(),
                squadId,
                alice,
                BigDecimal.valueOf(100),
                "Test expense",
                LocalDateTime.now(),
                parts
        );

        when(expenseStoragePort.getExpensesBySquadId(squadId)).thenReturn(List.of(expense));

        List<DebtSettlement> settlements = settleDebtsUseCase.settleDebts(squadId);

        assertThat(settlements).hasSize(2);

        assertThat(settlements).containsExactlyInAnyOrder(
                new DebtSettlement(bob, alice, BigDecimal.valueOf(20.00).setScale(2)),
                new DebtSettlement(charlie, alice, BigDecimal.valueOf(40.00).setScale(2))
        );
    }
}
