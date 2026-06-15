package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.UpdateLiveTracksRequest;
import com.verby.indp.domain.creator.dto.response.DjLiveListenersResponse;
import com.verby.indp.domain.creator.service.DjLiveService;
import com.verby.indp.global.resolver.LoginCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dj/live")
@RequiredArgsConstructor
public class DjLiveController {

    private final DjLiveService djLiveService;

    @PostMapping("/start")
    public ResponseEntity<Void> startLive(@LoginCreator Creator creator) {
        djLiveService.startLive(creator);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stopLive(@LoginCreator Creator creator) {
        djLiveService.stopLive(creator);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listeners")
    public ResponseEntity<DjLiveListenersResponse> getListeners(@LoginCreator Creator creator) {
        return ResponseEntity.ok(djLiveService.getListeners(creator));
    }

    @PutMapping("/tracks")
    public ResponseEntity<Void> updateLiveTracks(
        @LoginCreator Creator creator,
        @RequestBody UpdateLiveTracksRequest request
    ) {
        djLiveService.updateLiveTracks(creator, request);
        return ResponseEntity.ok().build();
    }
}
