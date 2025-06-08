package com.github.dustinbarnes.connect_four_demo.backend.repository;

import com.github.dustinbarnes.connect_four_demo.backend.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import org.jooq.Record;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class UserRepository {
    @Autowired
    private DSLContext dsl;

    public Optional<UserEntity> findUserById(Long id) {
        Record record = dsl.fetchOne("SELECT * FROM users WHERE id = ?", id);
        return Optional.ofNullable(mapToUserEntity(record));
    }

    public Optional<UserEntity> findByUsername(String username) {
        Record record = dsl.fetchOne("SELECT * FROM users WHERE username = ?", username);
        return Optional.ofNullable(mapToUserEntity(record));
    }

    public Optional<UserEntity> createUser(UserEntity user) {
        dsl.insertInto(table("users"))
            .set(field("username"), user.getUsername())
            .set(field("password_hash"), user.getPasswordHash())
            .execute();

        // Fetch the newly created user to return
        return findByUsername(user.getUsername());
    }

    private UserEntity mapToUserEntity(Record record) {
        return switch(record) {
            case null -> null;
            default -> new UserEntity(
                record.get("id", Long.class),
                record.get("username", String.class),
                record.get("password_hash", String.class),
                record.get("created_at", LocalDateTime.class)
            );
        };
    }
}
