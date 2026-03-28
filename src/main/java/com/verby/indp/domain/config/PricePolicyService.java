package com.verby.indp.domain.config;

import com.verby.indp.domain.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricePolicyService {

    private final PricePolicyRepository pricePolicyRepository;

    public PricePolicy getByPolicyKey(String policyKey) {
        return pricePolicyRepository.findByPolicyKey(policyKey)
            .orElseThrow(() -> new NotFoundException("존재하지 않은 요금 정책 입니다. policy key: " + policyKey));
    }
}
