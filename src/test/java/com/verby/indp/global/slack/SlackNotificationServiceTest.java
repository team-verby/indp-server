package com.verby.indp.global.slack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.store.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SlackNotificationServiceTest {

    private final SlackNotificationService slackNotificationService = new SlackNotificationService();

    @Test
    @DisplayName("handlePlaylistRegenerationRequest : 플레이리스트 재생성 요청을 처리한다.")
    void handlePlaylistRegenerationRequest() {
        Store store = Mockito.mock(Store.class);
        given(store.getName()).willReturn("카페 공명");
        given(store.getStoreId()).willReturn(1L);

        Exception exception = catchException(
            () -> slackNotificationService.handlePlaylistRegenerationRequest(store));

        assertThat(exception).isNull();
    }
}
