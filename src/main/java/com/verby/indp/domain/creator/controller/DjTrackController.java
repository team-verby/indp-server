package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.DjTrackUploadUrlRequest;
import com.verby.indp.domain.creator.dto.request.RegisterDjTrackRequest;
import com.verby.indp.domain.creator.dto.response.DjTrackResponse;
import com.verby.indp.domain.creator.dto.response.DjTrackUploadUrlResponse;
import com.verby.indp.domain.creator.dto.response.FindDjTracksResponse;
import com.verby.indp.domain.creator.service.DjTrackService;
import com.verby.indp.global.resolver.LoginCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/dj/tracks")
@RequiredArgsConstructor
public class DjTrackController {

    private final DjTrackService djTrackService;

    @GetMapping
    public ResponseEntity<FindDjTracksResponse> getTracks(@LoginCreator Creator creator) {
        return ResponseEntity.ok(djTrackService.getTracks(creator));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DjTrackResponse> uploadTrack(
        @LoginCreator Creator creator,
        @RequestParam MultipartFile file,
        @RequestParam(required = false, defaultValue = "0:00") String duration,
        @RequestParam(required = false, defaultValue = "0") int secs
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(djTrackService.uploadTrack(creator, file, duration, secs));
    }

    @PostMapping("/upload-url")
    public ResponseEntity<DjTrackUploadUrlResponse> createUploadUrl(
        @LoginCreator Creator creator,
        @RequestBody DjTrackUploadUrlRequest request
    ) {
        return ResponseEntity.ok(djTrackService.createUploadUrl(request));
    }

    @PostMapping("/register")
    public ResponseEntity<DjTrackResponse> registerTrack(
        @LoginCreator Creator creator,
        @RequestBody RegisterDjTrackRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(djTrackService.registerTrack(creator, request));
    }

    @DeleteMapping("/{trackId}")
    public ResponseEntity<Void> deleteTrack(
        @LoginCreator Creator creator,
        @PathVariable long trackId
    ) {
        djTrackService.deleteTrack(creator, trackId);
        return ResponseEntity.ok().build();
    }
}
