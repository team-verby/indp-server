package com.verby.indp.domain.subscription.controller;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.subscription.dto.request.AddSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.AddRenewalSubscriptionResponse;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SubscriptionControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/owner/stores/{storeId}/subscriptions 실행 시")
    class FindSubscriptions {

        @Test
        @DisplayName("성공 : 구독 목록을 조회한다.")
        void findSubscriptions() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            FindSubscriptionsResponse response = new FindSubscriptionsResponse(List.of());
            given(subscriptionService.findSubscriptions(any(), eq(1L))).willReturn(response);

            // when
            ResultActions resultActions = mockMvc.perform(
                get("/api/owner/stores/{storeId}/subscriptions", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        responseFields(
                            fieldWithPath("subscriptions").type(ARRAY).description("구독 목록")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("POST /api/owner/stores/{storeId}/subscriptions 실행 시")
    class AddSubscription {

        @Test
        @DisplayName("성공 : 구독을 추가한다.")
        void addSubscription() throws Exception {
            // given
            Owner owner = owner();
            givenOwnerAuth(owner);

            AddRenewalSubscriptionResponse response = new AddRenewalSubscriptionResponse(
                "INDP-20260101-uuid", 180000, "인디피_구독_카페공명");
            given(subscriptionService.orderRenewalSubscription(any(), eq(1L), any())).willReturn(response);

            AddSubscriptionRequest request = new AddSubscriptionRequest(1L, 12);

            // when
            ResultActions resultActions = mockMvc.perform(
                post("/api/owner/stores/{storeId}/subscriptions", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(
                    restDocs.document(
                        pathParameters(
                            parameterWithName("storeId").description("매장 ID")
                        ),
                        requestFields(
                            fieldWithPath("planId").type(NUMBER).description("플랜 ID"),
                            fieldWithPath("usagePeriod").type(NUMBER).description("구독 개월 수")
                        ),
                        responseFields(
                            fieldWithPath("orderId").type(STRING).description("주문 ID"),
                            fieldWithPath("amount").type(NUMBER).description("결제 금액 (원)"),
                            fieldWithPath("orderName").type(STRING).description("주문명")
                        )
                    )
                );
        }
    }
}
