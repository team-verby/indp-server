package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.recommendation.SongRecommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class PlaylistSseService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 30000)
    public void sendHeartbeat() {
        emitters.forEach((storeId, storeEmitters) ->
            storeEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                } catch (IOException e) {
                    removeEmitter(storeId, emitter);
                }
            })
        );
    }

    public SseEmitter subscribe(long storeId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(storeId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeEmitter(storeId, emitter));
        emitter.onTimeout(() -> removeEmitter(storeId, emitter));
        emitter.onError(e -> removeEmitter(storeId, emitter));
        return emitter;
    }

    public void sendSongRecommended(SongRecommendation recommendation, PlaylistSong playlistSong) {
        long storeId = recommendation.getStore().getStoreId();
        List<SseEmitter> storeEmitters = emitters.get(storeId);
        if (storeEmitters == null || storeEmitters.isEmpty()) {
            return;
        }
        Map<String, Object> data = Map.of(
            "type", "SONG_RECOMMENDED",
            "title", recommendation.getTitle(),
            "artist", recommendation.getArtist(),
            "vid", recommendation.getVid(),
            "playOrder", playlistSong.getPlayOrder(),
            "refereeName", recommendation.getRefereeName()
        );
        storeEmitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("SONG_RECOMMENDED").data(data));
            } catch (IOException e) {
                removeEmitter(storeId, emitter);
            }
        });
    }

    private void removeEmitter(long storeId, SseEmitter emitter) {
        List<SseEmitter> storeEmitters = emitters.get(storeId);
        if (storeEmitters != null) {
            storeEmitters.remove(emitter);
        }
    }
}
