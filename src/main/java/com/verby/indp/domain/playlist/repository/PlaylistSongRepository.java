package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findAllByPlaylistPlaylistIdOrderByPlayOrder(Long playlistId);

    void deleteAllByPlaylist(Playlist playlist);
}
