package com.verby.indp.domain.plan.dto.response;

import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;
import com.verby.indp.domain.plan.PlanFeature;

import java.util.Comparator;
import java.util.List;

public record FindPlansResponse(List<PlanItem> plans) {

    public static FindPlansResponse from(List<Plan> plans) {
        return new FindPlansResponse(plans.stream().map(PlanItem::from).toList());
    }

    private record PlanItem(
        Long planId,
        String type,
        String subtitle,
        String description,
        int monthlyPrice,
        int discountRate,
        List<String> features
    ) {
        private static PlanItem from(Plan plan) {
            int discountRate = plan.getDiscounts().stream()
                .filter(PlanDiscount::isActive)
                .findFirst()
                .map(PlanDiscount::getDiscountRate)
                .orElse(0);

            List<String> features = plan.getFeatures().stream()
                .sorted(Comparator.comparingInt(PlanFeature::getSortOrder))
                .map(PlanFeature::getFeatureLabel)
                .toList();

            return new PlanItem(plan.getPlanId(), plan.getType(), plan.getSubtitle(),
                plan.getDescription(), plan.getMonthlyPrice(), discountRate, features);
        }
    }
}
