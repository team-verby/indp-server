package com.verby.indp.domain.store;

import static jakarta.persistence.EnumType.STRING;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.vo.Address;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.store.vo.StoreName;
import com.verby.indp.domain.theme.Theme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Enumerated(STRING)
    @Column(name = "region")
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

    public String getName() {
        return name.getName();
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
            .toList();
    }

    public List<String> getSongForms() {
        return songForms.stream()
            .map(StoreSongForm::getSongForm)
            .toList();
    }
}
