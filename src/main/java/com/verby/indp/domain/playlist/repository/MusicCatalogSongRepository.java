package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicCatalogSongRepository extends JpaRepository<MusicCatalogSong, Long> {

    List<MusicCatalogSong> findAllByOrderByMoodAscPositionAsc();
}
