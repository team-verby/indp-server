package com.verby.indp.domain.creator.scheduler;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.creator.service.DjLiveService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DjAutoLiveSchedulerTest {

    @InjectMocks
    private DjAutoLiveScheduler djAutoLiveScheduler;

    @Mock
    private DjLiveService djLiveService;

    @Test
    @DisplayName("syncAutoLive : 자동 라이브 동기화 서비스를 호출한다.")
    void syncAutoLive() {
        // given
        willDoNothing().given(djLiveService).syncAutoLive();

        // when
        djAutoLiveScheduler.syncAutoLive();

        // then
        then(djLiveService).should().syncAutoLive();
    }
}
