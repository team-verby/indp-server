package com.verby.indp.domain.store;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
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
@Table(name = "store", uniqueConstraints = @UniqueConstraint(name = "uk_store_name", columnNames = "name"))
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
        validateStoreApply(storeApply);
        validateOwner(owner);
        validateName(name);
        validateAddress(address);
        validateCustomerAgeGroup(customerAgeGroup);
        validateLighting(lighting);
        validateStoreMusic(storeMusic);
        validateVibes(vibes);
        validateBusinessHours(businessHours);
        validateNoDuplicateDayOfWeek(businessHours);
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

    public void update(String name, String industry, String address, String customerAgeGroup,
        Integer lighting,
        StoreMusic storeMusic, List<Vibe> vibes, List<BusinessHour> businessHours,
        List<String> photoUrls) {
        this.storeMusic = storeMusic;
        this.name = name;
        this.industry = industry;
        this.address = address;
        this.customerAgeGroup = customerAgeGroup;
        this.lighting = lighting;
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
        this.businessHours.clear();
        businessHours.stream()
            .map(bh -> new StoreBusinessHour(this, bh.dayOfWeek(), bh.openTime(), bh.closeTime(),
                bh.isClosed()))
            .forEach(this.businessHours::add);
    }

    private void validateStoreApply(StoreApply storeApply) {
        if (storeApply == null) {
            throw new BadRequestException("storeApply는 필수입니다.");
        }
    }

    private void validateOwner(Owner owner) {
        if (owner == null) {
            throw new BadRequestException("owner는 필수입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name은 필수입니다.");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new BadRequestException("address는 필수입니다.");
        }
    }

    private void validateCustomerAgeGroup(String customerAgeGroup) {
        if (customerAgeGroup == null || customerAgeGroup.isBlank()) {
            throw new BadRequestException("customerAgeGroup은 필수입니다.");
        }
    }

    private void validateLighting(Integer lighting) {
        if (lighting == null || lighting < 0) {
            throw new BadRequestException("lighting은 0 이상이어야 합니다.");
        }
    }

    private void validateStoreMusic(StoreMusic storeMusic) {
        if (storeMusic == null) {
            throw new BadRequestException("storeMusic은 필수입니다.");
        }
    }

    private void validateVibes(List<Vibe> vibes) {
        if (vibes == null || vibes.isEmpty()) {
            throw new BadRequestException("vibes는 필수이며 비어있을 수 없습니다.");
        }
    }

    private void validateBusinessHours(List<BusinessHour> businessHours) {
        if (businessHours == null || businessHours.isEmpty()) {
            throw new BadRequestException("businessHours는 필수이며 비어있을 수 없습니다.");
        }
    }

    private void validateNoDuplicateDayOfWeek(List<BusinessHour> businessHours) {
        long distinctCount = businessHours.stream()
            .map(BusinessHour::dayOfWeek)
            .distinct()
            .count();
        if (distinctCount != businessHours.size()) {
            throw new BadRequestException("중복된 요일이 존재합니다.");
        }
    }
}
