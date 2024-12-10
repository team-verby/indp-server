package com.verby.indp.domain.region.service;

import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.store.dto.response.FindRegionsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @InjectMocks
    private RegionService regionService;

    @Mock
    private RegionRepository regionRepository;

    @Nested
    @DisplayName("findRegions 메서드 실행 시")
    class FindRegions {

        @Test
        @DisplayName("성공 : 매장 지역 목록을 조회한다.")
        void findRegions() {
            // given
            List<Region> regions = List.of(region("서울"), region("경기"));
            FindRegionsResponse expected = FindRegionsResponse.from(regions);
            when(regionRepository.findAllByOrderBySequenceAsc()).thenReturn(regions);

            // when
            FindRegionsResponse result = regionService.findRegions();

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

}
