package com.verby.indp.global.infrastructure;

import com.verby.indp.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final WebClient webClient;

    public <T> T post(HttpEntity httpEntity, String url, Class<T> clazz) {
        try {
            ResponseEntity<T> response = webClient
                .post()
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(requireNonNull(httpEntity.getBody())))
                .retrieve()
                .toEntity(clazz)
                .block();

            return requireNonNull(response).getBody();
        } catch (Exception exception) {
            throw new ExternalApiException("외부 API 호출 과정에서 오류가 발생했습니다." + exception.getMessage());
        }
    }
}
