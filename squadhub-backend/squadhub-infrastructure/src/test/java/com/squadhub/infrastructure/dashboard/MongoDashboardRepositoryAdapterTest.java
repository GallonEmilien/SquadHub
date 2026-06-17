package com.squadhub.infrastructure.dashboard;

import com.squadhub.infrastructure.expense.ExpenseDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
class MongoDashboardRepositoryAdapterTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDashboardRepositoryAdapter dashboardRepositoryAdapter;

    @Test
    void getTopSpenders_should_return_correct_aggregation() {
        UUID squadId = UUID.randomUUID();
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        mongoTemplate.insert(new ExpenseDocument(
                UUID.randomUUID(), squadId, user1, BigDecimal.valueOf(100), "exp1", LocalDateTime.now(), List.of()
        ));
        mongoTemplate.insert(new ExpenseDocument(
                UUID.randomUUID(), squadId, user2, BigDecimal.valueOf(50), "exp2", LocalDateTime.now(), List.of()
        ));
        mongoTemplate.insert(new ExpenseDocument(
                UUID.randomUUID(), squadId, user1, BigDecimal.valueOf(25), "exp3", LocalDateTime.now(), List.of()
        ));

        Map<UUID, BigDecimal> topSpenders = dashboardRepositoryAdapter.getTopSpenders(squadId);

        assertThat(topSpenders).hasSize(2);
        assertThat(topSpenders.get(user1)).isEqualByComparingTo(BigDecimal.valueOf(125));
        assertThat(topSpenders.get(user2)).isEqualByComparingTo(BigDecimal.valueOf(50));
    }
}
