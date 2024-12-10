package com.verby.indp.domain.store;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.vo.Address;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.store.vo.StoreName;
import com.verby.indp.domain.theme.Theme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @Getter
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Embedded
    private StoreName name;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StoreTheme> themes = new ArrayList<>();

    @OneToMany(mappedBy = "store", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StoreSongForm> songForms = new ArrayList<>();

    public Store(
        String name,
        String address,
        Region region,
        List<String> imageUrls,
        List<Theme> themes,
        List<SongForm> songForms
    ) {
        this.name = new StoreName(name);
        this.address = new Address(address);
        this.region = region;
        this.images = imageUrls.stream()
            .map(imageUrl -> new StoreImage(this, imageUrl))
            .toList();
        this.themes = themes.stream()
            .map(theme -> new StoreTheme(this, theme))
            .toList();
        this.songForms = songForms.stream()
            .map(songForm -> new StoreSongForm(this, songForm))
            .toList();
    }

    public void update(
        String name,
        String address,
        Region region,
        List<String> imageUrls,
        List<Theme> themes,
        List<SongForm> songForms
    ) {
        this.name = new StoreName(name);
        this.address = new Address(address);
        this.region = region;
        updateImages(imageUrls);
        updateThemes(themes);
        updateSongForms(songForms);
    }

    private void updateSongForms(List<SongForm> updatedSongForms) {
        List<StoreSongForm> existSongForms = this.songForms.stream()
            .filter(songForm -> updatedSongForms.contains(songForm.getSongForm()))
            .toList();

        List<StoreSongForm> newSongForms = updatedSongForms.stream()
            .map(songForm -> new StoreSongForm(this, songForm))
            .filter(storeSongForm -> !this.songForms.contains(storeSongForm))
            .toList();

        this.songForms.clear();
        this.songForms.addAll(existSongForms);
        this.songForms.addAll(newSongForms);
    }

    private void updateThemes(List<Theme> updatedThemes) {
        List<StoreTheme> existThemes = this.themes.stream()
            .filter(theme -> updatedThemes.contains(theme.getTheme()))
            .toList();

        List<StoreTheme> newThemes = updatedThemes.stream()
            .map(theme -> new StoreTheme(this, theme))
            .filter(storeTheme -> !this.themes.contains(storeTheme))
            .toList();

        this.themes.clear();
        this.themes.addAll(existThemes);
        this.themes.addAll(newThemes);
    }

    private void updateImages(List<String> imageUrls) {
        String nowImageUrl = images.get(0).getImageUrl();
        String imageUrl = imageUrls.get(0);
        if (!nowImageUrl.equals(imageUrl)) {
            images.clear();
            images.add(new StoreImage(this, imageUrl));
        }
    }

    public String getName() {
        return name.getName();
    }

    public String getRegion() {
        return region.getRegion();
    }

    public String getAddress() {
        return address.getAddress();
    }

    public List<String> getImage() {
        return images.stream()
            .map(StoreImage::getImageUrl)
            .toList();
    }

    public List<String> getThemes() {
        return themes.stream()
            .map(StoreTheme::getTheme)
            .map(Theme::getName)
            .toList();
    }

    public List<String> getSongForms() {
        return songForms.stream()
            .map(StoreSongForm::getSongForm)
            .map(SongForm::getName)
            .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Store store = (Store) o;
        return Objects.equals(storeId, store.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(storeId);
    }
}
