package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.service.StoreSseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreSseController {

    private final StoreSseService storeSseService;

    @GetMapping(value = "/{storeId}/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable long storeId) {
        return storeSseService.subscribe(storeId);
    }
}
