package com.verby.indp.domain.store;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.playlist.Playlist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "store")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_apply_id")
    private StoreApply storeApply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Column(name = "name")
    private String name;

    @Column(name = "industry")
    private String industry;

    @Column(name = "address")
    private String address;

    @Column(name = "customer_age_group")
    private String customerAgeGroup;

    @Column(name = "lighting")
    private Integer lighting; // 조명 색온도 (1000~10000K)

    @Column(name = "suspended", nullable = false)
    private boolean suspended = false;

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreMusic storeMusic;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayMethod> playMethods = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreMood> moods = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreBusinessHour> businessHours = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorePhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreMusicTimePreference> musicTimePreferences = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreGenre> genres = new ArrayList<>();

    public Store(
        StoreApply storeApply,
        Owner owner,
        String name,
        String industry,
        String address,
        String customerAgeGroup,
        Integer lighting
    ) {
        this.storeApply = storeApply;
        this.owner = owner;
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;
    }

    public void setStoreMusic(StoreMusic storeMusic) {
        this.storeMusic = storeMusic;
    }

}
