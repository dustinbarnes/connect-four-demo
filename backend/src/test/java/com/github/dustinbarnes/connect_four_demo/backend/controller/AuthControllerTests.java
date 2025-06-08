package com.github.dustinbarnes.connect_four_demo.backend.controller;

import com.github.dustinbarnes.connect_four_demo.backend.model.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = {
    "DELETE FROM users WHERE username LIKE 'test-%';"
})
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_success() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("test-user");
        request.setPassword("test-pass");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser_success() throws Exception {
        // First, register the user
        AuthRequest request = new AuthRequest();
        request.setUsername("test-loginuser");
        request.setPassword("loginpass");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // Then, login
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void registerUser_conflict() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("test-conflictuser");
        request.setPassword("pass");
        // Register once
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        // Register again
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void loginUser_invalidUsername() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("");
        request.setPassword("badpass");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_invalidPassword() throws Exception {
        // First, register the user
        AuthRequest request = new AuthRequest();
        request.setUsername("test-loginuser");
        request.setPassword("loginpass");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // Then, login
        request.setPassword("wrongpass");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void isLoggedIn_withValidToken_returnsOk() throws Exception {
        // Register and login to get a token
        AuthRequest request = new AuthRequest();
        request.setUsername("test-jwtuser");
        request.setPassword("test-jwtpass");
        String token = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        // Extract token from response JSON
        String jwt = objectMapper.readTree(token).get("token").asText();
        // Call /auth/is-logged-in with Bearer token
        mockMvc.perform(get("/auth/is-logged-in")
                .header("Authorization", "Bearer " + jwt))
            .andExpect(status().isOk());
    }

    @Test
    void isLoggedIn_withInvalidToken_returnsForbidden() throws Exception {
        mockMvc.perform(get("/auth/is-logged-in")
                .header("Authorization", "Bearer invalidtoken"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void isLoggedIn_withoutToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/auth/is-logged-in"))
            .andExpect(status().isUnauthorized());
    }
}
