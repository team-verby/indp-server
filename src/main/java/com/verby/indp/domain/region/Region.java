package com.verby.indp.domain.region;

import com.verby.indp.domain.region.vo.RegionName;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Embedded
    private RegionName name;

    @Column(name = "sequence")
    private Integer sequence = Integer.MAX_VALUE;

    public Region(String name) {
        this.name = new RegionName(name);
    }

    public String getRegion() {
        return name.getName();
    }
}
