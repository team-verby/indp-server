package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest;
import com.verby.indp.domain.playlist.dto.response.FindMoodScheduleResponse;
import com.verby.indp.domain.playlist.dto.response.SaveMoodScheduleResponse;
import com.verby.indp.domain.playlist.service.AdminMoodScheduleService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/mood-schedule")
@RequiredArgsConstructor
public class AdminMoodScheduleController {

    private final AdminMoodScheduleService moodScheduleService;

    @GetMapping
    public ResponseEntity<FindMoodScheduleResponse> findMoodSchedule() {
        return ResponseEntity.ok(moodScheduleService.findMoodSchedule());
    }

    @PutMapping
    public ResponseEntity<SaveMoodScheduleResponse> updateMoodSchedule(
        @RequestBody UpdateMoodScheduleRequest request) {
        LocalDateTime savedAt = moodScheduleService.updateMoodSchedule(request);
        return ResponseEntity.ok(new SaveMoodScheduleResponse(savedAt));
    }
}
