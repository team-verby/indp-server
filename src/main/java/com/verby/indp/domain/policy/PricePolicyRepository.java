package com.verby.indp.domain.policy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricePolicyRepository extends JpaRepository<PricePolicy, String> {
    Optional<PricePolicy> findByPolicyKey(String policyKey);
}
