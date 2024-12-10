package com.verby.indp.domain.region.repository;

import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.vo.RegionName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(RegionName name);

    List<Region> findAllByOrderBySequenceAsc();
}
