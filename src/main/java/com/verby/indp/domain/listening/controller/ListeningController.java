package com.verby.indp.domain.listening.controller;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.listening.dto.request.HeartbeatRequest;
import com.verby.indp.domain.listening.service.ListeningService;
import com.verby.indp.global.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/listening")
@RequiredArgsConstructor
public class ListeningController {

    private final ListeningService listeningService;

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> heartbeat(
        @LoginUser User user,
        @RequestBody HeartbeatRequest request
    ) {
        listeningService.heartbeat(user, request);
        return ResponseEntity.ok().build();
    }
}
