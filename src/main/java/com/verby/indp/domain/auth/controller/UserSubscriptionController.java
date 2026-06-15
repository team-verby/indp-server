package com.verby.indp.domain.auth.controller;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.response.UserPaymentsResponse;
import com.verby.indp.domain.auth.dto.response.UserSubscriptionResponse;
import com.verby.indp.domain.auth.service.UserSubscriptionService;
import com.verby.indp.global.resolver.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @GetMapping("/subscription")
    public ResponseEntity<UserSubscriptionResponse> getSubscription(@LoginUser User user) {
        return ResponseEntity.ok(userSubscriptionService.getSubscription(user));
    }

    @GetMapping("/payments")
    public ResponseEntity<UserPaymentsResponse> getPayments(@LoginUser User user) {
        return ResponseEntity.ok(userSubscriptionService.getPayments(user));
    }
}
