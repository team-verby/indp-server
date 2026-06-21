package com.verby.indp.global.seed;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.repository.PlanRepository;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreApply;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [임시] Plan A 구독(ACTIVE) 테스트 계정을 운영에 시드하기 위한 서비스.
 * 테스트 계정 생성 후 global/seed 패키지째 제거 예정.
 */
@Service
@RequiredArgsConstructor
public class SeedService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final PlanRepository planRepository;
    private final Clock clock;

    @Transactional
    public Map<String, Object> createActivePlanAAccount(String storeName, String loginId,
        String password) {
        if (ownerRepository.existsByLoginId(loginId)) {
            throw new BadRequestException("이미 사용 중인 loginId입니다.");
        }
        if (storeRepository.existsByName(storeName)) {
            throw new BadRequestException("이미 존재하는 매장 이름입니다.");
        }

        Owner owner = ownerRepository.save(
            new Owner(loginId, password, "테스트 사장님", "010-0000-0000"));

        List<BusinessHour> businessHours = List.of(
            new BusinessHour(1, LocalTime.of(9, 0), LocalTime.of(18, 0), false));

        StoreApply storeApply = new StoreApply("테스트 사장님", "010-0000-0000");
        StoreMusic storeMusic = new StoreMusic(
            "기타", "테스트 재생곡", null,
            PlaylistType.MUSIC_RECOMMENDED, Tempo.NORMAL, null,
            List.of(PlayMethod.Method.BLUETOOTH),
            List.of(), List.of(),
            businessHours);

        Store store = new Store(
            storeApply, owner, storeName, "카페", "서울특별시 테스트구 테스트로 1",
            "20-30대", 3, storeMusic, List.of(Vibe.MODERN),
            businessHours, List.of());

        Plan planA = planRepository.findAll().stream()
            .filter(p -> p.getType() == Plan.PlanType.PLAN_A)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("PLAN_A 요금제가 존재하지 않습니다."));

        Payment payment = new Payment(PaymentType.SUBSCRIPTION, "인디피_구독_" + storeName, 4400);
        StoreSubscription subscription =
            new StoreSubscription(planA, payment, 1, LocalDate.now(clock));
        subscription.updateStatus(SubscriptionStatus.ACTIVE);
        store.addSubscription(subscription);
        storeRepository.save(store);

        return Map.of(
            "loginId", owner.getLoginId(),
            "password", password,
            "storeId", store.getStoreId(),
            "planType", "PLAN_A",
            "subscriptionStatus", "ACTIVE"
        );
    }
}
