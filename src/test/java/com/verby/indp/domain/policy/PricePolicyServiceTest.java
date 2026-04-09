package com.verby.indp.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.NotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PricePolicyServiceTest {

    @InjectMocks
    private PricePolicyService pricePolicyService;

    @Mock
    private PricePolicyRepository pricePolicyRepository;

    @Nested
    @DisplayName("getByPolicyKey 메서드 실행 시")
    class GetByPolicyKey {

        @Test
        @DisplayName("성공 : 요금 정책을 반환한다.")
        void getByPolicyKey() {
            PricePolicy pricePolicy = Mockito.mock(PricePolicy.class);
            given(pricePolicyRepository.findByPolicyKey("recommendation_fee"))
                .willReturn(Optional.of(pricePolicy));

            PricePolicy result = pricePolicyService.getByPolicyKey("recommendation_fee");

            assertThat(result).isEqualTo(pricePolicy);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 정책 키이면 예외를 던진다.")
        void getByPolicyKeyWithNotExist() {
            given(pricePolicyRepository.findByPolicyKey("unknown"))
                .willReturn(Optional.empty());

            Exception exception = catchException(
                () -> pricePolicyService.getByPolicyKey("unknown"));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
