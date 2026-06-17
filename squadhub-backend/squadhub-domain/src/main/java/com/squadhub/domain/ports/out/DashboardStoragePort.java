package com.squadhub.domain.ports.out;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface DashboardStoragePort {
    Map<UUID, BigDecimal> getTopSpenders(UUID squadId);
}
