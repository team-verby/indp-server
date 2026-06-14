package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DjRevenueService {

    public DjRevenueResponse getRevenue(Creator creator) {
        // 정산 정책 미확정 — null 반환
        return new DjRevenueResponse(null, null, null);
    }
}
