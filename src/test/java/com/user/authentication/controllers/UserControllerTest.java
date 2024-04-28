package com.user.authentication.controllers;

import com.user.authentication.payload.request.JwtRequest;
import com.user.authentication.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Test
    void login() {
        when(userService.getResponse(any(JwtRequest.class))).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));
        assertDoesNotThrow(() -> userController.login(new JwtRequest()));
    }
}