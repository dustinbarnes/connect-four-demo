package com.github.dustinbarnes.connect_four_demo.backend.repository;

import com.github.dustinbarnes.connect_four_demo.backend.entity.UserEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(statements = {
    "DELETE FROM users WHERE username LIKE 'test-%';"
})
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateAndFindUser() {
        UserEntity user = new UserEntity(null, "test-user", "hashedpass", null);
        Optional<UserEntity> created = userRepository.createUser(user);
        assertTrue(created.isPresent());
        assertEquals("test-user", created.get().getUsername());
        assertEquals("hashedpass", created.get().getPasswordHash());
        assertNotNull(created.get().getCreatedAt());
    }

    @Test
    void testFindByUsername_userExists() {
        UserEntity user = new UserEntity(null, "test-findme", "pass", null);
        userRepository.createUser(user);
        Optional<UserEntity> found = userRepository.findByUsername("test-findme");
        assertTrue(found.isPresent());
        assertEquals("test-findme", found.get().getUsername());
    }

    @Test
    void testFindByUsername_userDoesNotExist() {
        Optional<UserEntity> found = userRepository.findByUsername("test-doesnotexist");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindUserById() {
        UserEntity user = new UserEntity(null, "test-byid", "pass", null);
        Optional<UserEntity> created = userRepository.createUser(user);
        assertTrue(created.isPresent());
        Long id = created.get().getId();
        Optional<UserEntity> found = userRepository.findUserById(id);
        assertTrue(found.isPresent());
        assertEquals("test-byid", found.get().getUsername());
    }
}
