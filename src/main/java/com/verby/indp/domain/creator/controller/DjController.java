package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.ChangePasswordRequest;
import com.verby.indp.domain.creator.dto.request.UpdateDjProfileRequest;
import com.verby.indp.domain.creator.dto.response.DjProfileResponse;
import com.verby.indp.domain.creator.service.DjService;
import com.verby.indp.global.resolver.LoginCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dj")
@RequiredArgsConstructor
public class DjController {

    private final DjService djService;

    @GetMapping("/profile")
    public ResponseEntity<DjProfileResponse> getProfile(@LoginCreator Creator creator) {
        return ResponseEntity.ok(djService.getProfile(creator));
    }

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfile(
        @LoginCreator Creator creator,
        @ModelAttribute UpdateDjProfileRequest request
    ) {
        djService.updateProfile(creator, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
        @LoginCreator Creator creator,
        @RequestBody ChangePasswordRequest request
    ) {
        djService.changePassword(creator, request);
        return ResponseEntity.ok().build();
    }
}
