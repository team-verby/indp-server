package com.verby.indp.domain.region.fixture;

import com.verby.indp.domain.region.Region;

public class RegionFixture {

    public static Region region(String name) {
        return new Region(name);
    }

}
