package com.verby.indp.domain.store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "store_photo")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StorePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_photo_id")
    private Long storePhotoId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "is_main")
    private boolean isMain;

    public StorePhoto(Store store, String imageUrl, int sortOrder, boolean isMain) {
        this.store = store;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.isMain = isMain;
    }
}
