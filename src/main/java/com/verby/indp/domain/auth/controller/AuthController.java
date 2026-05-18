package com.verby.indp.domain.auth.controller;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.request.RefreshRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.dto.response.RefreshResponse;
import com.verby.indp.domain.auth.service.AdminService;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.auth.service.AuthTokenService;
import com.verby.indp.global.resolver.LoginAdmin;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AdminService adminService;
    private final OwnerService ownerService;
    private final AuthTokenService authTokenService;

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> adminLogin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authTokenService.refresh(request));
    }

    @PostMapping("/admin/logout")
    public ResponseEntity<Void> adminLogout(@LoginAdmin Admin admin) {
        adminService.logout(admin.getAdminId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/owner/login")
    public ResponseEntity<LoginResponse> ownerLogin(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ownerService.login(request));
    }

    @PostMapping("/owner/logout")
    public ResponseEntity<Void> ownerLogout(@LoginOwner Owner owner) {
        ownerService.logout(owner.getOwnerId());
        return ResponseEntity.noContent().build();
    }
}
