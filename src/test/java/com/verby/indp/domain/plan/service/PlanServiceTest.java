package com.verby.indp.domain.plan.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.dto.response.FindPlansResponse;
import com.verby.indp.domain.plan.repository.PlanRepository;
import com.verby.indp.fixture.PlanFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @InjectMocks
    private PlanService planService;

    @Mock
    private PlanRepository planRepository;

    @Nested
    @DisplayName("getPlans 메서드 실행 시")
    class GetPlans {

        @Test
        @DisplayName("성공 : 플랜 목록을 반환한다.")
        void getPlans() {
            // given
            Plan plan = PlanFixture.planA();

            given(planRepository.findAll()).willReturn(List.of(plan));
            FindPlansResponse expected = FindPlansResponse.from(List.of(plan));

            // when
            FindPlansResponse result = planService.getPlans();

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("getPlan 메서드 실행 시")
    class GetPlan {

        @Test
        @DisplayName("성공 : 플랜을 반환한다.")
        void getPlan() {
            // given
            Plan plan = PlanFixture.planA();
            given(planRepository.findById(1L)).willReturn(Optional.of(plan));

            // when
            Plan result = planService.getPlan(1L);

            // then
            assertThat(result).isEqualTo(plan);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 플랜이면 예외를 던진다.")
        void getPlanWithNotExist() {
            // given
            given(planRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> planService.getPlan(999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
