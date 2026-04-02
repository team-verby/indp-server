package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.recommendation.SongRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendSongRecommended(SongRecommendation recommendation, PlaylistSong playlistSong) {
        messagingTemplate.convertAndSend(
            "/topic/stores/" + recommendation.getStore().getStoreId(),
            Map.of(
                "type", "SONG_RECOMMENDED",
                "title", recommendation.getTitle(),
                "artist", recommendation.getArtist(),
                "vid", recommendation.getVid(),
                "playOrder", playlistSong.getPlayOrder(),
                "refereeName", recommendation.getRefereeName()
            )
        );
    }
}