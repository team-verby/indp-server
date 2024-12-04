package com.verby.indp.domain.theme;

import com.verby.indp.domain.theme.vo.ThemeName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theme")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Theme {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long themeId;

    @Embedded
    private ThemeName name;

    public Theme(String name) {
        this.name = new ThemeName(name);
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(themeId, theme.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(themeId);
    }
}
