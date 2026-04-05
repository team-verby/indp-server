package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.MusicGenre;

public record GenreItem(
    MusicGenre.Genre genre,
    MusicGenre.PreferenceType preferenceType
) {}
