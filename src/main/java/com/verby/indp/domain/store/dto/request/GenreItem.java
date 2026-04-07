package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.MusicGenre;
import com.verby.indp.domain.store.vo.Genre;

public record GenreItem(
    Genre genre,
    MusicGenre.PreferenceType preferenceType
) {

}
