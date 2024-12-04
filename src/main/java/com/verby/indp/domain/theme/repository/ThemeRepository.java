package com.verby.indp.domain.theme.repository;

import com.verby.indp.domain.theme.Theme;
import com.verby.indp.domain.theme.vo.ThemeName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findByName(ThemeName name);
}
