package com.verby.indp.fixture;

import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import java.time.LocalTime;
import java.util.List;

public class StoreMusicFixture {

    public static StoreMusic storeMusic() {
        return new StoreMusic(
            "유튜브 뮤직",
            "인디, 어쿠스틱",
            "너무 빠른 비트 제외",
            PlaylistType.CONSISTENT_MOOD,
            Tempo.CALM,
            "아늑하고 조용한",
            List.of(PlayMethod.Method.BLUETOOTH),
            List.of(),
            List.of(),
            List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false))
        );
    }
}
