package com.verby.indp.domain.store.dto.request;

import com.verby.indp.domain.store.MusicGenre.PreferenceType;
import com.verby.indp.domain.store.vo.Genre;

import java.util.List;

public record UpdateGenresByAdminRequest(
    List<GenrePreferenceItem> genres
) {

    public record GenrePreferenceItem(Genre genre, PreferenceType preferenceType) {
    }
}
