package com.verby.indp.domain.song.repository;

import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.song.vo.SongFormName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongFormRepository extends JpaRepository<SongForm, Long>  {

    Optional<SongForm> findByName(SongFormName name);
}
