package com.squadhub.domain.usecase;

import com.squadhub.domain.DebtSettlement;
import com.squadhub.domain.Expense;
import com.squadhub.domain.ports.in.SettleDebtsUseCase;
import com.squadhub.domain.ports.out.ExpenseStoragePort;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
class SettleDebtsUseCaseImpl implements SettleDebtsUseCase {

    private final ExpenseStoragePort expenseStoragePort;

    SettleDebtsUseCaseImpl(ExpenseStoragePort expenseStoragePort) {
        this.expenseStoragePort = expenseStoragePort;
    }

    @Override
    public List<DebtSettlement> settleDebts(UUID squadId) {
        List<Expense> expenses = expenseStoragePort.getExpensesBySquadId(squadId);
        Map<UUID, BigDecimal> balances = calculateBalances(expenses);
        return settleBalances(balances);
    }

    private Map<UUID, BigDecimal> calculateBalances(List<Expense> expenses) {
        Map<UUID, BigDecimal> balances = new HashMap<>();

        for (Expense expense : expenses) {
            balances.merge(expense.paidByMemberId(), expense.amount(), BigDecimal::add);

            BigDecimal totalWeight = expense.parts().stream()
                    .filter(p -> !p.excluded())
                    .map(Expense.ExpensePart::weight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            for (Expense.ExpensePart part : expense.parts()) {
                if (!part.excluded()) {
                    BigDecimal share = expense.amount()
                            .multiply(part.weight())
                            .divide(totalWeight, 2, RoundingMode.HALF_UP);
                    balances.merge(part.memberId(), share.negate(), BigDecimal::add);
                }
            }
        }
        return balances;
    }

    private List<DebtSettlement> settleBalances(Map<UUID, BigDecimal> balances) {
        Map<UUID, BigDecimal> debtors = balances.entrySet().stream()
                .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) < 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<UUID, BigDecimal> creditors = balances.entrySet().stream()
                .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<DebtSettlement> settlements = new ArrayList<>();

        for (Map.Entry<UUID, BigDecimal> debtorEntry : debtors.entrySet()) {
            UUID debtor = debtorEntry.getKey();
            BigDecimal debtorAmount = debtorEntry.getValue().abs();

            for (Map.Entry<UUID, BigDecimal> creditorEntry : creditors.entrySet()) {
                UUID creditor = creditorEntry.getKey();
                BigDecimal creditorAmount = creditorEntry.getValue();

                if (creditorAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal settlementAmount = debtorAmount.min(creditorAmount);
                    settlements.add(new DebtSettlement(debtor, creditor, settlementAmount));

                    debtorAmount = debtorAmount.subtract(settlementAmount);
                    creditors.put(creditor, creditorAmount.subtract(settlementAmount));

                    if (debtorAmount.compareTo(BigDecimal.ZERO) == 0) {
                        log.warn("Skipping expense calculation because total weight of parts is zero.");
                        break;
                    }
                }
            }
        }
        return settlements;
    }
}
