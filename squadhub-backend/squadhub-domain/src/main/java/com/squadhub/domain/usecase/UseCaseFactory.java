package com.squadhub.domain.usecase;

import com.squadhub.domain.ports.in.AnonymizeUserUseCase;
import com.squadhub.domain.ports.in.SettleDebtsUseCase;
import com.squadhub.domain.ports.out.ExpenseStoragePort;
import com.squadhub.domain.ports.out.TokenVerifierPort;
import com.squadhub.domain.ports.out.UserRepositoryPort;

public class UseCaseFactory {
    public static SettleDebtsUseCase createSettleDebtsUseCase(ExpenseStoragePort expenseStoragePort) {
        return new SettleDebtsUseCaseImpl(expenseStoragePort);
    }

    public static AnonymizeUserUseCase createAnonymizeUserUseCase(UserRepositoryPort userRepositoryPort) {
        return new AnonymizeUserUseCaseImpl(userRepositoryPort);
    }

    public static AuthenticateUseCase createAuthenticateUseCase(UserRepositoryPort userRepositoryPort, TokenVerifierPort tokenVerifierPort) {
        return new AuthenticateUseCaseImpl(userRepositoryPort, tokenVerifierPort);
    }
}
