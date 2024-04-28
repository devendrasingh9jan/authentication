package com.user.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.authentication.Repository.RoleRepository;
import com.user.authentication.Repository.UserRepository;
import com.user.authentication.config.JwtService;
import com.user.authentication.exception.BadCredentialsException;
import com.user.authentication.models.Role;
import com.user.authentication.models.User;
import com.user.authentication.models.UserCred;
import com.user.authentication.payload.request.JwtRequest;
import com.user.authentication.payload.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testResponseValidCredentialsReturnsJwtResponse() {
        JwtRequest request = new JwtRequest("test@example.com", "password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(request.getEmail(), request.getPassword(), new HashSet<>());
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("testToken");
        ResponseEntity<JwtResponse> response = userService.getResponse(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testToken", response.getBody().getJwtToken());
        assertEquals("test@example.com", response.getBody().getUsername());
    }

    @Test
    void testResponseInvalidCredentialsThrowsBadCredentialsException() {
        JwtRequest request = new JwtRequest("test@example.com", "password");
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);
        assertThrows(BadCredentialsException.class, () -> userService.getResponse(request));
    }

    @Test
    void testUserCredentialsValidJsonSavesUser() {
        String userCredentialsJson = "{\"email\":\"test@example.com\"}";
        when(roleRepository.save(any())).thenReturn(new Role());
        when(userRepository.save(any())).thenReturn(new User());
        userService.saveUserCredentials(userCredentialsJson);
        verify(roleRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testSaveUserCredentialsInvalidJsonThrowsRuntimeException() {
        String userCredentialsJson = "Invalid JSON";
        assertThrows(RuntimeException.class, () -> userService.saveUserCredentials(userCredentialsJson));
    }
}