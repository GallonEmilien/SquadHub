package com.squadhub.infrastructure.expense;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseMongoRepository extends MongoRepository<ExpenseDocument, UUID> {
    List<ExpenseDocument> findBySquadId(UUID squadId);
}
