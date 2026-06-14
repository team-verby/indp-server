package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DjRevenueServiceTest {

    @InjectMocks
    private DjRevenueService djRevenueService;

    @Nested
    @DisplayName("getRevenue 메서드 실행 시")
    class GetRevenue {

        @Test
        @DisplayName("성공 : 정산 미확정으로 null을 반환한다.")
        void getRevenue() {
            Creator creator = creatorWithId(1L);
            DjRevenueResponse response = djRevenueService.getRevenue(creator);
            assertThat(response.thisMonthEstimate()).isNull();
            assertThat(response.totalPaid()).isNull();
            assertThat(response.nextPayoutDate()).isNull();
        }
    }
}
