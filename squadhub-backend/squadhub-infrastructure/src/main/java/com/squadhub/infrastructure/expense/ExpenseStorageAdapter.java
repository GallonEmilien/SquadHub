package com.squadhub.infrastructure.expense;

import com.squadhub.domain.Expense;
import com.squadhub.domain.ports.out.ExpenseStoragePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ExpenseStorageAdapter implements ExpenseStoragePort {

    private final ExpenseMongoRepository repository;

    public ExpenseStorageAdapter(ExpenseMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Expense> getExpensesBySquadId(UUID squadId) {
        return repository.findBySquadId(squadId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Expense toDomain(ExpenseDocument doc) {
        List<Expense.ExpensePart> parts = doc.parts().stream()
                .map(p -> new Expense.ExpensePart(p.memberId(), p.weight(), p.excluded()))
                .collect(Collectors.toList());

        return new Expense(
                doc.id(),
                doc.squadId(),
                doc.paidByMemberId(),
                doc.amount(),
                doc.description(),
                doc.date(),
                parts
        );
    }
}
