package com.user.authentication.client;

import com.user.authentication.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "USER-PROFILE-SERVICE")
public interface UserProfileClient {
    @GetMapping("/api/user/user-details/email")
    ResponseEntity<Optional<User>> findByEmail(@RequestParam("email") String email);
}
