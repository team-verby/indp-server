package com.verby.indp.global.seed;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 임시 시드 엔드포인트 — 개인 구독자(Plan A app_user) 테스트 계정 생성용. 검증 후 즉시 제거.
 */
@RestController
@RequiredArgsConstructor
public class SeedController {

    private static final String SEED_TOKEN = "indp-seed-7Qx2PlanA-2026";

    private final SeedService seedService;

    @PostMapping("/api/internal/seed/plan-a-user")
    public ResponseEntity<?> seedPlanAUser(
        @RequestParam String token,
        @RequestParam String loginId,
        @RequestParam String password,
        @RequestParam String name,
        @RequestParam String email
    ) {
        if (!SEED_TOKEN.equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "forbidden"));
        }
        return ResponseEntity.ok(seedService.createActivePlanAUser(loginId, password, name, email));
    }
}
