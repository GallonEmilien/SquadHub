package com.squadhub.config;

import com.squadhub.domain.ports.in.AnonymizeUserUseCase;
import com.squadhub.domain.ports.in.SettleDebtsUseCase;
import com.squadhub.domain.ports.out.ExpenseStoragePort;
import com.squadhub.domain.ports.out.TokenVerifierPort;
import com.squadhub.domain.ports.out.UserRepositoryPort;
import com.squadhub.domain.usecase.AuthenticateUseCase;
import com.squadhub.domain.usecase.UseCaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HexagonArchitectureConfig {

    @Bean
    public SettleDebtsUseCase settleDebtsUseCase(ExpenseStoragePort expenseStoragePort) {
        return UseCaseFactory.createSettleDebtsUseCase(expenseStoragePort);
    }

    @Bean
    public AnonymizeUserUseCase anonymizeUserUseCase(UserRepositoryPort userRepositoryPort) {
        return UseCaseFactory.createAnonymizeUserUseCase(userRepositoryPort);
    }

    @Bean
    public AuthenticateUseCase authenticateUseCase(UserRepositoryPort userRepositoryPort, TokenVerifierPort tokenVerifierPort) {
        return UseCaseFactory.createAuthenticateUseCase(userRepositoryPort, tokenVerifierPort);
    }
}