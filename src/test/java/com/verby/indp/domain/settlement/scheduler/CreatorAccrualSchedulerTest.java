package com.verby.indp.domain.settlement.scheduler;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.settlement.service.CreatorAccrualService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatorAccrualSchedulerTest {

    @InjectMocks
    private CreatorAccrualScheduler creatorAccrualScheduler;

    @Mock
    private CreatorAccrualService creatorAccrualService;

    @Test
    @DisplayName("accruePreviousMonth : 전월 적립 서비스를 호출한다.")
    void accruePreviousMonth() {
        willDoNothing().given(creatorAccrualService).accruePreviousMonth();

        creatorAccrualScheduler.accruePreviousMonth();

        then(creatorAccrualService).should().accruePreviousMonth();
    }
}
