package com.verby.indp.domain.playlist;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.LocalDate;
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

    @Column(name = "is_playing")
    private boolean isPlaying = false;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> songs = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "playlist")
    private Store store;

    public Playlist(List<PlaylistSong> songs) {
        this.songs = songs;
        songs.forEach(song -> song.setPlaylist(this));
    }

    public void addSong(PlaylistSong song) {
        songs.add(song);
        song.setPlaylist(this);
    }

    public void connected() {
        boolean isOpen = isOpen();
        if (isOpen) {
            isPlaying = true;
        }
    }

    public void disconnected() {
        this.isPlaying = false;
    }

    private boolean isOpen() {
        int todayDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        LocalTime now = LocalTime.now();

        return store.getBusinessHours().stream()
            .filter(bh -> bh.getDayOfWeek() == todayDayOfWeek && !bh.isClosed())
            .anyMatch(bh -> {
                LocalTime start = bh.getOpenTime().minusMinutes(30);
                LocalTime end = bh.getCloseTime();
                return !now.isBefore(start) && now.isBefore(end);
            });
    }
}
