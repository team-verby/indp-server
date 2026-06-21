package com.verby.indp.global.seed;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 임시 시드 — 개인 구독자(Plan A app_user) 테스트 계정 생성용.
 * 운영에서 청취자 카운트/정산 집계를 검증한 뒤 즉시 제거한다.
 */
@Service
@RequiredArgsConstructor
public class SeedService {

    private static final int USAGE_PERIOD = 1;
    private static final int AMOUNT = 4400;

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional
    public Map<String, Object> createActivePlanAUser(
        String loginId, String password, String name, String email) {
        User user = new User(loginId, passwordEncoder.encode(password), name, email);
        userRepository.save(user);

        Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A 라이트 요금제 · 월간", AMOUNT);
        paymentRepository.save(payment);

        UserSubscription subscription = new UserSubscription(user, payment, USAGE_PERIOD);
        subscription.activate(LocalDate.now(clock));
        userSubscriptionRepository.save(subscription);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("loginId", loginId);
        result.put("password", password);
        result.put("userId", user.getUserId());
        result.put("planType", "PLAN_A");
        result.put("storeId", null);
        result.put("subscriptionStatus", subscription.getStatus().name());
        result.put("startDate", subscription.getStartDate().toString());
        result.put("endDate", subscription.getEndDate().toString());
        return result;
    }
}
