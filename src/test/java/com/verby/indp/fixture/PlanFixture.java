package com.verby.indp.fixture;

import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.Plan.PlanType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import org.springframework.test.util.ReflectionTestUtils;

public class PlanFixture {

    public static Plan planA() {
        Plan plan = createPlan();
        ReflectionTestUtils.setField(plan, "planId", 1L);
        ReflectionTestUtils.setField(plan, "type", PlanType.PLAN_A);
        ReflectionTestUtils.setField(plan, "monthlyPrice", 15000);
        ReflectionTestUtils.setField(plan, "discounts", new ArrayList<>());
        return plan;
    }

    public static Plan planB() {
        Plan plan = createPlan();
        ReflectionTestUtils.setField(plan, "planId", 2L);
        ReflectionTestUtils.setField(plan, "type", PlanType.PLAN_B);
        ReflectionTestUtils.setField(plan, "monthlyPrice", 39000);
        ReflectionTestUtils.setField(plan, "discounts", new ArrayList<>());
        return plan;
    }

    public static Plan planC() {
        Plan plan = createPlan();
        ReflectionTestUtils.setField(plan, "planId", 3L);
        ReflectionTestUtils.setField(plan, "type", PlanType.PLAN_C);
        ReflectionTestUtils.setField(plan, "monthlyPrice", 33000);
        ReflectionTestUtils.setField(plan, "discounts", new ArrayList<>());
        return plan;
    }

    private static Plan createPlan() {
        try {
            Constructor<Plan> constructor = Plan.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
