package com.verby.indp.fixture;

import com.verby.indp.domain.policy.PricePolicy;
import java.lang.reflect.Constructor;
import org.springframework.test.util.ReflectionTestUtils;

public class PricePolicyFixture {

    public static PricePolicy pricePolicy() {
        return pricePolicyWithAmount(3000);
    }

    public static PricePolicy pricePolicyWithAmount(int amount) {
        try {
            Constructor<PricePolicy> constructor = PricePolicy.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            PricePolicy pricePolicy = constructor.newInstance();
            ReflectionTestUtils.setField(pricePolicy, "policyKey", "recommendation_fee");
            ReflectionTestUtils.setField(pricePolicy, "amount", amount);
            return pricePolicy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
