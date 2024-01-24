package com.verby.indp.domain.store;

import com.verby.indp.domain.theme.Theme;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_theme")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_theme_id")
    private long storeThemeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public StoreTheme(Store store, Theme theme) {
        this.store = store;
        this.theme = theme;
    }

    public String getTheme() {
        return theme.getName();
    }
}
