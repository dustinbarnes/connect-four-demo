package com.github.dustinbarnes.connect_four_demo.backend.service;

import com.github.dustinbarnes.connect_four_demo.backend.entity.UserEntity;
import com.github.dustinbarnes.connect_four_demo.backend.model.AuthRequest;
import com.github.dustinbarnes.connect_four_demo.backend.model.LoginResponse;
import com.github.dustinbarnes.connect_four_demo.backend.repository.UserRepository;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;   
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<LoginResponse> registerUser(AuthRequest authRequest) {
        UserEntity createUser = new UserEntity(
            null, 
            authRequest.getUsername(),
            passwordEncoder.encode(authRequest.getPassword()),
            null
        );

        Optional<UserEntity> userEntity = userRepository.createUser(createUser);
        return userEntity.map(user -> {
            LoginResponse response = new LoginResponse();
            response.setToken(generateToken(user));
            return response;
        });
    }

    public Optional<LoginResponse> login(AuthRequest authRequest) {
        Optional<UserEntity> user = userRepository.findByUsername(authRequest.getUsername());
        if (user.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), user.get().getPasswordHash())) {
            return Optional.empty();
        }

        return Optional.of(new LoginResponse(generateToken(user.get())));
    }

    private String generateToken(UserEntity user) {
        return jwtService.generateToken(user);
    }
}
