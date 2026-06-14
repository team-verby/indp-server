package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.creator.dto.response.UserPaymentsResponse;
import com.verby.indp.domain.creator.dto.response.UserSubscriptionResponse;
import com.verby.indp.domain.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserSubscriptionService {

    public UserSubscriptionResponse getSubscription(User user) {
        // Plan A 구독 정보 — 신청 흐름 구현 후 연동 예정
        throw new NotFoundException("구독 정보가 없습니다.");
    }

    public UserPaymentsResponse getPayments(User user) {
        return new UserPaymentsResponse(List.of());
    }
}
