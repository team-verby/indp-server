package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
