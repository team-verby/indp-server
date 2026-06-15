package com.verby.indp.domain.auth.controller;

import com.verby.indp.domain.auth.dto.response.FindUsersResponse;
import com.verby.indp.domain.auth.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/users")
    public ResponseEntity<FindUsersResponse> findUsers() {
        return ResponseEntity.ok(adminUserService.findUsers());
    }
}
