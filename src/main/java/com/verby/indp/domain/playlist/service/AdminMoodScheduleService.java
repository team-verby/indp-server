package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.StoreMoodSchedule;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.Slot;
import com.verby.indp.domain.playlist.dto.request.UpdateMoodScheduleRequest.StoreSchedule;
import com.verby.indp.domain.playlist.dto.response.FindMoodScheduleResponse;
import com.verby.indp.domain.playlist.repository.StoreMoodScheduleRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMoodScheduleService {

    private final StoreMoodScheduleRepository storeMoodScheduleRepository;

    public FindMoodScheduleResponse findMoodSchedule() {
        List<StoreMoodSchedule> rows =
            storeMoodScheduleRepository.findAllByOrderByStoreNameAscHourAsc();
        return FindMoodScheduleResponse.from(latestSavedAt(rows), rows);
    }

    /**
     * 매장별 시간대-무드 매핑 전체를 통째로 교체한다(기존 전체 삭제 후 재삽입).
     * 프론트의 매핑(apData)이 전체 매장 매핑을 한 번에 저장하는 방식과 1:1로 대응한다.
     *
     * @return 저장된 시각(저장된 행이 없으면 null). 전체 재삽입이라 행들의 createdAt 최댓값이 곧 마지막 저장 시각이다.
     */
    @Transactional
    public LocalDateTime updateMoodSchedule(UpdateMoodScheduleRequest request) {
        storeMoodScheduleRepository.deleteAllInBatch();

        if (request.schedules() == null) {
            return null;
        }

        List<StoreMoodSchedule> rows = new ArrayList<>();
        for (StoreSchedule schedule : request.schedules()) {
            if (schedule == null || schedule.storeName() == null
                || schedule.storeName().isBlank() || schedule.slots() == null) {
                continue;
            }
            for (Slot slot : schedule.slots()) {
                if (slot == null || slot.mood() == null || slot.mood().isBlank()) {
                    continue;
                }
                if (slot.hour() < 0 || slot.hour() > 23) {
                    continue;
                }
                rows.add(new StoreMoodSchedule(schedule.storeName(), slot.hour(), slot.mood()));
            }
        }
        storeMoodScheduleRepository.saveAll(rows);
        // saveAll 영속화 시 @CreatedDate(AuditingEntityListener)가 각 행 createdAt을 채운다.
        return latestSavedAt(rows);
    }

    private LocalDateTime latestSavedAt(List<StoreMoodSchedule> rows) {
        return rows.stream()
            .map(StoreMoodSchedule::getCreatedAt)
            .filter(Objects::nonNull)
            .max(Comparator.naturalOrder())
            .orElse(null);
    }
}
