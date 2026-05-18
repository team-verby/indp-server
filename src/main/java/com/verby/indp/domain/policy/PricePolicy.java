package com.verby.indp.domain.policy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "price_policy")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PricePolicy {

    @Id
    @Column(name = "policy_key")
    private String policyKey;

    @Column(name = "amount", nullable = false)
    private int amount;
}
