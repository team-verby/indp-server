package com.verby.indp.domain.auth.controller;

import com.verby.indp.domain.auth.dto.request.UserApplicationRequest;
import com.verby.indp.domain.auth.dto.response.UserApplicationResponse;
import com.verby.indp.domain.auth.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApplicationController {

    private final UserApplicationService userApplicationService;

    @PostMapping("/applications")
    public ResponseEntity<UserApplicationResponse> apply(@RequestBody UserApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userApplicationService.apply(request));
    }
}
