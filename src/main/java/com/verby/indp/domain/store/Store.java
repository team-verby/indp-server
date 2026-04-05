package com.verby.indp.domain.store;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
    @JoinColumn(name = "owner_id")
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "store_music_id")
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
                 String customerAgeGroup, Integer lighting, StoreMusic storeMusic, List<Vibe> vibes,
                 List<BusinessHour> businessHours, List<String> photoUrls) {
        this.storeApply = storeApply;
        this.storeMusic = storeMusic;
        this.owner = owner;
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;

        setBusinessHours(businessHours);
        setPhotos(photoUrls);
        setVibes(vibes);
    }

    public void update(String name, String industry, String address, String customerAgeGroup, Integer lighting,
                       StoreMusic storeMusic, List<Vibe> vibes, List<BusinessHour> businessHours, List<String> photoUrls) {
        this.storeMusic = storeMusic;
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;
        this.businessHours.clear();

        setBusinessHours(businessHours);
        setPhotos(photoUrls);
        setVibes(vibes);
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

    private void setVibes(List<Vibe> vibes) {
        this.vibes = vibes.stream()
            .map(vibe -> new StoreVibe(this, vibe))
            .toList();
    }

    private void setPhotos(List<String> photoUrls) {
        this.photos = IntStream.range(0, photoUrls.size())
            .mapToObj(i -> new StorePhoto(this, photoUrls.get(i), i, i == 0))
            .toList();
    }

    private void setBusinessHours(List<BusinessHour> businessHours) {
        this.businessHours = businessHours.stream()
            .map(bh -> new StoreBusinessHour(this, bh.dayOfWeek(), bh.openTime(), bh.closeTime(), bh.isClosed()))
            .toList();
    }
}
