package com.verby.indp.domain.theme.fixture;

import com.verby.indp.domain.theme.Theme;

public class ThemeFixture {

    private static final String THEME_NAME = "ThemeName";

    public static Theme theme() {
        return new Theme(THEME_NAME);
    }

}
