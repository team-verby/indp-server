package com.verby.indp.domain.auth.controller;

import com.verby.indp.domain.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/check-id")
    public ResponseEntity<Void> checkId(@RequestParam String loginId) {
        userService.checkLoginIdDuplicate(loginId);
        return ResponseEntity.ok().build();
    }
}
