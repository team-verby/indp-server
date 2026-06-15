package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.request.UserApplicationRequest;
import com.verby.indp.domain.auth.dto.response.UserApplicationResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.ConflictException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserApplicationService {

    private static final int MONTHLY_RATE = 4400;
    private static final String ORDER_NAME_PREFIX = "Plan A 라이트 요금제 · ";

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserApplicationResponse apply(UserApplicationRequest request) {
        validateUsagePeriod(request.usagePeriod());
        if (userRepository.existsByLoginId(request.loginId())) {
            throw new ConflictException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("이미 사용 중인 이메일입니다.");
        }

        User user = new User(
            request.loginId(),
            passwordEncoder.encode(request.password()),
            request.name(),
            request.email()
        );
        userRepository.save(user);

        int amount = MONTHLY_RATE * request.usagePeriod();
        String orderName = ORDER_NAME_PREFIX + periodLabel(request.usagePeriod());

        Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, orderName, amount);
        paymentRepository.save(payment);

        userSubscriptionRepository.save(new UserSubscription(user, payment, request.usagePeriod()));

        return new UserApplicationResponse(payment.getOrderId(), amount, orderName);
    }

    private void validateUsagePeriod(int usagePeriod) {
        if (usagePeriod != 1 && usagePeriod != 12) {
            throw new BadRequestException("usagePeriod는 1(월간) 또는 12(연간)만 허용됩니다.");
        }
    }

    private String periodLabel(int usagePeriod) {
        return usagePeriod == 1 ? "월간" : "연간";
    }
}
