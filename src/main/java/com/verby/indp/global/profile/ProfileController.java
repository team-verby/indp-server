package com.verby.indp.global.profile;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final Environment environment;

    @GetMapping("/profile")
    public String profile() {
        return Arrays.stream(environment.getActiveProfiles())
            .findFirst()
            .orElse("");
    }
}
