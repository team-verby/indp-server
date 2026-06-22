package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.dto.request.CreatorApplicationRequest;
import com.verby.indp.domain.creator.service.CreatorApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator-applications")
@RequiredArgsConstructor
public class CreatorApplicationController {

    private final CreatorApplicationService creatorApplicationService;

    @PostMapping
    public ResponseEntity<Void> apply(@RequestBody CreatorApplicationRequest request) {
        creatorApplicationService.apply(request);
        return ResponseEntity.ok().build();
    }
}
