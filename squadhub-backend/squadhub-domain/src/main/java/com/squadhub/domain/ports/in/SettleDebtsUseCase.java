package com.squadhub.domain.ports.in;

import com.squadhub.domain.DebtSettlement;

import java.util.List;
import java.util.UUID;

public interface SettleDebtsUseCase {
    List<DebtSettlement> settleDebts(UUID squadId);
}
