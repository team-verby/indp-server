package com.verby.indp.global.slack;

import static com.verby.indp.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.store.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlackNotificationServiceTest {

    private final SlackNotificationService slackNotificationService = new SlackNotificationService();

    @Test
    @DisplayName("handlePlaylistRegenerationRequest : 플레이리스트 재생성 요청을 처리한다.")
    void handlePlaylistRegenerationRequest() {
        // given
        Store store = store();

        // when
        Exception exception = catchException(
            () -> slackNotificationService.handlePlaylistRegenerationRequest(store));

        // then
        assertThat(exception).isNull();
    }
}
