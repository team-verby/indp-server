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
import java.util.Objects;
import lombok.Getter;
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

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public StoreTheme(Store store, Theme theme) {
        this.store = store;
        this.theme = theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreTheme that = (StoreTheme) o;
        return Objects.equals(store, that.store) && Objects.equals(theme,
            that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, theme);
    }
}
