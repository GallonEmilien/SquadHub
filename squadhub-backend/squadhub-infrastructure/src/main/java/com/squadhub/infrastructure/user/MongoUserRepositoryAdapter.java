package com.squadhub.infrastructure.user;

import com.squadhub.domain.models.User;
import com.squadhub.domain.ports.out.UserRepositoryPort;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class MongoUserRepositoryAdapter implements UserRepositoryPort {

    private final MongoTemplate mongoTemplate;

    public MongoUserRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        UserDoc doc = mongoTemplate.findOne(query, UserDoc.class, "users");
        return Optional.ofNullable(doc).map(d -> new User(d.id, d.email, d.name, d.avatar));
    }

    @Override
    public User save(User user) {
        UserDoc doc = new UserDoc(user.id(), user.email(), user.name(), user.avatar());
        mongoTemplate.save(doc, "users");
        return user;
    }

    @Override
    public void anonymizeUser(UUID userId, String anonymizedName) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update()
                .set("name", anonymizedName)
                .set("email", null)
                .set("avatar", null);
        mongoTemplate.updateFirst(query, update, "users");
    }

    @Document(collection = "users")
    private record UserDoc(@Id UUID id, String email, String name, String avatar) {}
}
