package com.user.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.authentication.Repository.RoleRepository;
import com.user.authentication.Repository.UserRepository;
import com.user.authentication.config.JwtService;
import com.user.authentication.exception.BadCredentialsException;
import com.user.authentication.models.Role;
import com.user.authentication.models.User;
import com.user.authentication.models.UserCred;
import com.user.authentication.payload.request.JwtRequest;
import com.user.authentication.payload.response.JwtResponse;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<JwtResponse> getResponse(JwtRequest request) {
        doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @KafkaListener(topics = "AUTH_TOPIC", groupId = "AUTH_GROUP")
    @Transient
    private void saveUserCredentials(String userCredentialsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserCred userCred = null;
        try {
            userCred = objectMapper.readValue(userCredentialsJson, UserCred.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        User user = new User();
        user.setEmail(userCred.getEmail());
        String encodedPassword = passwordEncoder.encode(userCred.getPassword());
        user.setPassword(encodedPassword);
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        roleRepository.save(role);
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }
}
