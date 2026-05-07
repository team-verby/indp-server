package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "playlist")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Playlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long playlistId;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("playOrder ASC")
    private List<PlaylistSong> songs = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "playlist")
    private Store store;

    public Playlist(List<PlaylistSong> songs) {
        validateSongs(songs);
        this.songs = songs;
        songs.forEach(song -> song.setPlaylist(this));
    }

    public void addSong(PlaylistSong song) {
        songs.add(song);
        song.setPlaylist(this);
    }

    private void validateSongs(List<PlaylistSong> songs) {
        if (songs == null || songs.isEmpty()) {
            throw new BadRequestException("songs는 필수이며 비어있을 수 없습니다.");
        }
    }
}
