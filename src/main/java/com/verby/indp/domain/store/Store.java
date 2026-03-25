package com.verby.indp.domain.store;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.playlist.Playlist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "store_apply_id")
    private StoreApply storeApply;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
    private int lighting;

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private StoreMusic storeMusic;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreVibe> vibes = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreBusinessHour> businessHours = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorePhoto> photos = new ArrayList<>();

    public Store(StoreApply storeApply, Owner owner, String name, String industry, String address,
                 String customerAgeGroup, int lighting, StoreMusic storeMusic, List<StoreVibe> vibes,
                 List<StoreBusinessHour> businessHours, List<StorePhoto> photos) {
        this.storeApply = storeApply;
        this.owner = owner;
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;
        setStoreMusic(storeMusic);
        setVibes(vibes);
        setBusinessHours(businessHours);
        setPhotos(photos);
    }

    public void assignPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    private void setStoreMusic(StoreMusic storeMusic) {
        this.storeMusic = storeMusic;
        storeMusic.setStore(this);
    }

    private void setVibes(List<StoreVibe> vibes) {
        this.vibes = vibes;
        vibes.forEach(vibe -> vibe.setStore(this));
    }

    private void setBusinessHours(List<StoreBusinessHour> businessHours) {
        this.businessHours = businessHours;
        businessHours.forEach(businessHour -> businessHour.setStore(this));
    }

    private void setPhotos(List<StorePhoto> photos) {
        this.photos = photos;
        photos.forEach(photo -> photo.setStore(this));
    }
}
