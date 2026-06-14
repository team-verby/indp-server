package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.dto.request.CreateCreatorRequest;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse.CreatorItem;
import com.verby.indp.domain.creator.service.AdminCreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/creators")
@RequiredArgsConstructor
public class AdminCreatorController {

    private final AdminCreatorService adminCreatorService;

    @PostMapping
    public ResponseEntity<Void> createCreator(@RequestBody CreateCreatorRequest request) {
        adminCreatorService.createCreator(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<FindCreatorsResponse> findCreators() {
        return ResponseEntity.ok(adminCreatorService.findCreators());
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorItem> findCreator(@PathVariable long creatorId) {
        return ResponseEntity.ok(adminCreatorService.findCreator(creatorId));
    }

    @PatchMapping("/{creatorId}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable long creatorId) {
        adminCreatorService.deactivate(creatorId);
        return ResponseEntity.ok().build();
    }
}
