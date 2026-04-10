package com.verby.indp.domain.recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import com.verby.indp.fixture.PaymentFixture;
import com.verby.indp.fixture.StoreFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SongRecommendationTest {

    private Store store() {
        return StoreFixture.store();
    }

    private Payment payment() {
        return PaymentFixture.payment();
    }

    @Nested
    @DisplayName("SongRecommendation 생성 시")
    class NewSongRecommendation {

        @Test
        @DisplayName("성공 : SongRecommendation을 생성한다.")
        void newSongRecommendation() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259,
                    "홍길동", payment()));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : store가 null이면 예외를 던진다.")
        void newSongRecommendationWithNullStore() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(null, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, "홍길동",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 blank이면 예외를 던진다.")
        void newSongRecommendationWithBlankTitle() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "  ", "성시경", "5zAEiu3SaO4", 259, "홍길동",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : artist가 blank이면 예외를 던진다.")
        void newSongRecommendationWithBlankArtist() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "  ", "5zAEiu3SaO4", 259, "홍길동",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : vid가 blank이면 예외를 던진다.")
        void newSongRecommendationWithBlankVid() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "성시경", "  ", 259, "홍길동",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 0 이하이면 예외를 던진다.")
        void newSongRecommendationWithNonPositivePlayTime() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 0, "홍길동",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : refereeName이 blank이면 예외를 던진다.")
        void newSongRecommendationWithBlankRefereeName() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, "  ",
                    payment()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : payment가 null이면 예외를 던진다.")
        void newSongRecommendationWithNullPayment() {
            // when
            Exception exception = catchException(() ->
                new SongRecommendation(store(), "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259,
                    "홍길동", null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("updateStatus 메서드 실행 시")
    class UpdateStatus {

        @Test
        @DisplayName("성공 : 상태를 변경한다.")
        void updateStatus() {
            // given
            SongRecommendation recommendation = new SongRecommendation(store(), "안녕 나의 사랑",
                "성시경", "5zAEiu3SaO4", 259, "홍길동", payment());

            // when
            recommendation.updateStatus(SongRecommendation.RecommendationStatus.RECOMMENDED);

            // then
            assertThat(recommendation.getStatus())
                .isEqualTo(SongRecommendation.RecommendationStatus.RECOMMENDED);
        }

        @Test
        @DisplayName("실패 : status가 null이면 예외를 던진다.")
        void updateStatusWithNull() {
            // given
            SongRecommendation recommendation = new SongRecommendation(store(), "안녕 나의 사랑",
                "성시경", "5zAEiu3SaO4", 259, "홍길동", payment());

            // when
            Exception exception = catchException(() -> recommendation.updateStatus(null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}
