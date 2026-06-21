package com.verby.indp.global.seed;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [임시] Plan A 구독(ACTIVE) 테스트 계정 시드용 엔드포인트.
 * 운영 테스트 계정 생성 후 global/seed 패키지째 제거 예정. 시크릿 토큰으로 보호.
 */
@RestController
@RequiredArgsConstructor
public class SeedController {

    private static final String SEED_TOKEN = "indp-seed-7Qx2PlanA-2026";

    private final SeedService seedService;

    @PostMapping("/api/internal/seed/plan-a")
    public ResponseEntity<Map<String, Object>> seedPlanA(
        @RequestParam String token,
        @RequestParam String storeName,
        @RequestParam String loginId,
        @RequestParam String password
    ) {
        if (!SEED_TOKEN.equals(token)) {
            return ResponseEntity.status(403).body(Map.of("error", "invalid token"));
        }
        return ResponseEntity.ok(seedService.createActivePlanAAccount(storeName, loginId, password));
    }
}
