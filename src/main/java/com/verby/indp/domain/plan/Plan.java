package com.verby.indp.domain.plan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "plan")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PlanType type;

    public enum PlanType {
        PLAN_A, PLAN_B, PLAN_C
    }

    @Column(name = "monthly_price")
    private int monthlyPrice;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanDiscount> discounts = new ArrayList<>();

}
