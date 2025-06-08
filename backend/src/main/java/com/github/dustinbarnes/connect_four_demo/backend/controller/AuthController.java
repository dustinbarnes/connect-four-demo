package com.github.dustinbarnes.connect_four_demo.backend.controller;

import com.github.dustinbarnes.connect_four_demo.backend.api.AuthApi;
import com.github.dustinbarnes.connect_four_demo.backend.model.AuthRequest;
import com.github.dustinbarnes.connect_four_demo.backend.model.LoginResponse;
import com.github.dustinbarnes.connect_four_demo.backend.service.UserService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<LoginResponse> authRegisterPost(AuthRequest authRequest) {
        if (!userService.findByUsername(authRequest.getUsername()).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Optional<LoginResponse> loginResponse = userService.registerUser(authRequest);
        if (loginResponse.isPresent()) {
            return new ResponseEntity<>(loginResponse.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> authLoginPost(AuthRequest authRequest) {
        Optional<LoginResponse> loginResponse = userService.login(authRequest);
        if (loginResponse.isPresent()) {
            return new ResponseEntity<>(loginResponse.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> authIsLoggedInGet() {
        // in theory based on securityconfig, this should only
        // be called if the user is authenticated. 

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
