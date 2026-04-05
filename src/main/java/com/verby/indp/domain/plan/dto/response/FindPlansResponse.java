package com.verby.indp.domain.plan.dto.response;

import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;

import java.util.List;

public record FindPlansResponse(List<PlanItem> plans) {

    public static FindPlansResponse from(List<Plan> plans) {
        return new FindPlansResponse(plans.stream().map(PlanItem::from).toList());
    }

    private record PlanItem(
        Long planId,
        String type,
        int monthlyPrice,
        int discountRate
    ) {
        private static PlanItem from(Plan plan) {
            int discountRate = plan.getDiscounts().stream()
                .filter(PlanDiscount::isActive)
                .findFirst()
                .map(PlanDiscount::getDiscountRate)
                .orElse(0);

            return new PlanItem(plan.getPlanId(), plan.getType(), plan.getMonthlyPrice(), discountRate);
        }
    }
}
