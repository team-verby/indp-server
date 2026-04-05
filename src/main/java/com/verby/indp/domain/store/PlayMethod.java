package com.verby.indp.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "play_method")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PlayMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "play_method_id")
    private Long playMethodId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_music_id")
    private StoreMusic storeMusic;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private Method method;

    public PlayMethod(StoreMusic storeMusic, Method method) {
        this.storeMusic = storeMusic;
        this.method = method;
    }

    public enum Method {
        BLUETOOTH,
        WIRED,
        OTHER
    }
}
