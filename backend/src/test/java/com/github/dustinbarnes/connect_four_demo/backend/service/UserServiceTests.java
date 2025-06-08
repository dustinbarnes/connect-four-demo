package com.github.dustinbarnes.connect_four_demo.backend.service;

import com.github.dustinbarnes.connect_four_demo.backend.entity.UserEntity;
import com.github.dustinbarnes.connect_four_demo.backend.model.AuthRequest;
import com.github.dustinbarnes.connect_four_demo.backend.model.LoginResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
@Sql(statements = {
    "DELETE FROM users WHERE username LIKE 'test-%';"
})
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    void registerAndFindUser() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("test-user");
        authRequest.setPassword("testpass");
        
        Optional<LoginResponse> response = userService.registerUser(authRequest);
        assertTrue(response.isPresent());
        assertNotNull(response.get().getToken());
    }

    @Test
    void existsByUsername() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("test-user");
        authRequest.setPassword("testpass");
        
        userService.registerUser(authRequest);
        
        assertTrue(userService.findByUsername("test-user").isPresent());
        assertTrue(userService.findByUsername("no-user").isEmpty());
    }
}
