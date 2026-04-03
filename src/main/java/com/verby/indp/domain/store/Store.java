package com.verby.indp.domain.store;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
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

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreSubscription> subscriptions = new ArrayList<>();

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

    public void update(String name, String industry, String address, String customerAgeGroup, int lighting,
                       List<StoreBusinessHour> businessHours, List<StorePhoto> photos, List<StoreVibe> vibes) {
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;

        this.businessHours.clear();
        businessHours.forEach(bh -> {
            bh.setStore(this);
            this.businessHours.add(bh);
        });

        this.photos.clear();
        photos.forEach(p -> {
            p.setStore(this);
            this.photos.add(p);
        });

        this.vibes.clear();
        vibes.forEach(v -> {
            v.setStore(this);
            this.vibes.add(v);
        });
    }

    public void assignPlaylist(Playlist playlist) {
        this.playlist = playlist;
        playlist.setStore(this);
    }

    public StoreSubscription getLatestSubscription() {
        return subscriptions.stream()
            .max(Comparator.comparing(StoreSubscription::getCreatedAt))
            .orElseThrow(() -> new NotFoundException("구독 정보가 없습니다."));
    }

    public Optional<StoreSubscription> findLatestPaidSubscription() {
        return subscriptions.stream()
                .filter(s -> s.getStatus() != SubscriptionStatus.PENDING_PAYMENT)
                .max(Comparator.comparing(StoreSubscription::getStartDate));
    }

    public boolean isInactive() {
        return subscriptions.stream()
            .noneMatch(s -> s.getStatus() == SubscriptionStatus.ACTIVE);
    }

    public void addSubscription(StoreSubscription subscription) {
        this.subscriptions.add(subscription);
        subscription.setStore(this);
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
