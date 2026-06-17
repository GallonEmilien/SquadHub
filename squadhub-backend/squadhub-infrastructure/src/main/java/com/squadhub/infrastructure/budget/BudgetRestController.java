package com.squadhub.infrastructure.budget;

import com.squadhub.domain.DebtSettlement;
import com.squadhub.domain.ports.in.SettleDebtsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/squads/{squadId}/budget")
public class BudgetRestController {

    private final SettleDebtsUseCase settleDebtsUseCase;

    public BudgetRestController(SettleDebtsUseCase settleDebtsUseCase) {
        this.settleDebtsUseCase = settleDebtsUseCase;
    }

    @GetMapping("/settlements")
    public List<DebtSettlement> getSettlements(@PathVariable UUID squadId) {
        return settleDebtsUseCase.settleDebts(squadId);
    }
}
