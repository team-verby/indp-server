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

    public record PlanItem(
        Long planId,
        String code,
        String subtitle,
        String description,
        int monthlyPrice,
        boolean isRecommended,
        DiscountInfo discount,
        List<String> features
    ) {
        public static PlanItem from(Plan plan) {
            DiscountInfo discount = plan.getDiscounts().stream()
                .filter(PlanDiscount::isActive)
                .findFirst()
                .map(d -> new DiscountInfo(d.getDiscountRate(),
                    (int) (plan.getMonthlyPrice() * (100 - d.getDiscountRate()) / 100.0)))
                .orElse(null);

            List<String> features = plan.getFeatures().stream()
                .sorted(Comparator.comparingInt(PlanFeature::getSortOrder))
                .map(PlanFeature::getFeatureLabel)
                .toList();

            return new PlanItem(plan.getPlanId(), plan.getCode(), plan.getSubtitle(),
                plan.getDescription(), plan.getMonthlyPrice(), plan.isRecommended(), discount, features);
        }
    }

    public record DiscountInfo(int discountRate, int discountedPrice) {
    }
}
