package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SongRecommendationPaymentConfirmHandlerTest {

    @InjectMocks
    private SongRecommendationPaymentConfirmHandler handler;

    @Mock
    private SongRecommendationService songRecommendationService;

    @Test
    @DisplayName("supportedType은 SONG_RECOMMENDATION을 반환한다.")
    void supportedType() {
        assertThat(handler.supportedType()).isEqualTo(PaymentType.SONG_RECOMMENDATION);
    }

    @Test
    @DisplayName("handle 호출 시 songRecommendationService.confirmPayment를 위임한다.")
    void handle() {
        Payment payment = new Payment("인디피_노래추천_카페공명", 3000);
        willDoNothing().given(songRecommendationService).confirmPayment(payment);

        handler.handle(payment);

        verify(songRecommendationService).confirmPayment(payment);
    }
}
