package com.verby.indp.domain.playlist;

import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "scheduled_playlist")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ScheduledPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduled_playlist_id")
    private Long scheduledPlaylistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UpdateStatus status = UpdateStatus.PENDING;

    @OneToMany(mappedBy = "scheduledPlaylist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledPlaylistSong> songs = new ArrayList<>();

    public enum UpdateStatus {
        PENDING, APPLIED
    }

    public ScheduledPlaylist(Store store, LocalDateTime scheduledAt, List<ScheduledPlaylistSong> songs) {
        this.store = store;
        this.scheduledAt = scheduledAt;
        songs.forEach(s -> {
            s.setScheduledPlaylist(this);
            this.songs.add(s);
        });
    }

    public void markApplied() {
        this.status = UpdateStatus.APPLIED;
    }
}
