package com.squadhub.infrastructure.dashboard;

import com.squadhub.domain.ports.out.DashboardStoragePort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class MongoDashboardRepositoryAdapter implements DashboardStoragePort {

    private final MongoTemplate mongoTemplate;

    public MongoDashboardRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<UUID, BigDecimal> getTopSpenders(UUID squadId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("squadId").is(squadId)),
                group("paidByMemberId").sum("amount").as("totalAmount"),
                project("totalAmount").and("_id").as("memberId")
        );

        AggregationResults<TopSpenderResult> results = mongoTemplate.aggregate(
                aggregation, "expenses", TopSpenderResult.class
        );

        return results.getMappedResults().stream()
                .collect(Collectors.toMap(TopSpenderResult::memberId, TopSpenderResult::totalAmount));
    }

    private record TopSpenderResult(UUID memberId, BigDecimal totalAmount) {}
}
