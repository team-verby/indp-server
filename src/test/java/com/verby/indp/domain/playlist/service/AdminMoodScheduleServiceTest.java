package com.verby.indp.domain.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.verby.indp.domain.playlist.StoreMoodSchedule;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.Slot;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.StoreSchedule;
import com.verby.indp.domain.playlist.dto.response.FindMoodScheduleResponse;
import com.verby.indp.domain.playlist.repository.StoreMoodScheduleRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminMoodScheduleServiceTest {

    @InjectMocks
    private AdminMoodScheduleService adminMoodScheduleService;

    @Mock
    private StoreMoodScheduleRepository storeMoodScheduleRepository;

    @Captor
    private ArgumentCaptor<List<StoreMoodSchedule>> rowsCaptor;

    @Nested
    @DisplayName("findMoodSchedule 메서드 실행 시")
    class FindMoodSchedule {

        @Test
        @DisplayName("성공 : 매장별로 그룹핑된 시간대-무드 매핑을 반환한다.")
        void findMoodSchedule() {
            // given
            given(storeMoodScheduleRepository.findAllByOrderByStoreNameAscHourAsc())
                .willReturn(List.of(
                    new StoreMoodSchedule("카페 공명 홍대점", 10, "모던한 팝"),
                    new StoreMoodSchedule("카페 공명 홍대점", 11, "모던한 팝")
                ));

            // when
            FindMoodScheduleResponse response = adminMoodScheduleService.findMoodSchedule();

            // then
            assertThat(response.schedules()).hasSize(1);
            assertThat(response.schedules().get(0).storeName()).isEqualTo("카페 공명 홍대점");
            assertThat(response.schedules().get(0).slots()).hasSize(2);
            assertThat(response.schedules().get(0).slots().get(0).hour()).isEqualTo(10);
            assertThat(response.schedules().get(0).slots().get(0).mood()).isEqualTo("모던한 팝");
        }
    }

    @Nested
    @DisplayName("updateMoodSchedule 메서드 실행 시")
    class UpdateMoodSchedule {

        @Test
        @DisplayName("성공 : 기존 매핑을 전체 삭제 후 새 매핑을 저장한다.")
        void updateMoodSchedule() {
            // given
            UpdateMoodScheduleRequest request = new UpdateMoodScheduleRequest(List.of(
                new StoreSchedule("카페 공명 홍대점", List.of(
                    new Slot(10, "모던한 팝"),
                    new Slot(11, "잔잔한 팝")
                ))
            ));

            // when
            adminMoodScheduleService.updateMoodSchedule(request);

            // then
            then(storeMoodScheduleRepository).should().deleteAllInBatch();
            then(storeMoodScheduleRepository).should().saveAll(rowsCaptor.capture());
            List<StoreMoodSchedule> saved = rowsCaptor.getValue();
            assertThat(saved).hasSize(2);
            assertThat(saved.get(0).getStoreName()).isEqualTo("카페 공명 홍대점");
            assertThat(saved.get(0).getHour()).isEqualTo(10);
            assertThat(saved.get(1).getMood()).isEqualTo("잔잔한 팝");
        }

        @Test
        @DisplayName("성공 : mood가 비었거나 hour가 범위를 벗어나거나 매장명이 빈 슬롯은 건너뛴다.")
        void updateMoodScheduleSkipsInvalidSlots() {
            // given
            UpdateMoodScheduleRequest request = new UpdateMoodScheduleRequest(List.of(
                new StoreSchedule("카페 공명 홍대점", List.of(
                    new Slot(10, "모던한 팝"),
                    new Slot(11, " "),      // 빈 무드 → 건너뜀
                    new Slot(99, "잔잔한 팝")  // 범위 밖 hour → 건너뜀
                )),
                new StoreSchedule(" ", List.of(new Slot(10, "모던한 팝")))  // 빈 매장명 → 건너뜀
            ));

            // when
            adminMoodScheduleService.updateMoodSchedule(request);

            // then
            then(storeMoodScheduleRepository).should().saveAll(rowsCaptor.capture());
            assertThat(rowsCaptor.getValue()).hasSize(1);
        }

        @Test
        @DisplayName("성공 : schedules가 null이면 전체 삭제만 수행한다.")
        void updateMoodScheduleWithNullSchedules() {
            // given
            UpdateMoodScheduleRequest request = new UpdateMoodScheduleRequest(null);

            // when
            adminMoodScheduleService.updateMoodSchedule(request);

            // then
            then(storeMoodScheduleRepository).should().deleteAllInBatch();
            then(storeMoodScheduleRepository).should(org.mockito.Mockito.never()).saveAll(anyList());
        }
    }
}
