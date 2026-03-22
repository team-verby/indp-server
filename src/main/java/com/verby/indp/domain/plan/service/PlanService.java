package com.verby.indp.domain.plan.service;

import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.dto.response.FindPlansResponse;
import com.verby.indp.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;

    public FindPlansResponse findPlans() {
        List<Plan> plans = planRepository.findAllByOrderByDisplayOrderAsc();
        return FindPlansResponse.from(plans);
    }
}
