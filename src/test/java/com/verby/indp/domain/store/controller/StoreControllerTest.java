package com.verby.indp.domain.store.controller;

import static com.verby.indp.domain.store.constant.Region.서울;
import static com.verby.indp.domain.store.fixture.StoreFixture.storesWithId;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.ResultActions;

class StoreControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 매장 목록을 조회한다.(간단 정보)")
    void findSimpleStores() throws Exception {
        // given
        int count = 10;
        int page = 0;
        int size = 2;

        List<Store> stores = storesWithId(List.of(), List.of(), count);
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> pageStores = new PageImpl<>(stores.subList(page * size, size), pageable, count);

        FindSimpleStoresResponse response = FindSimpleStoresResponse.from(pageStores);

        when(storeService.findSimpleStores(pageable)).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/main/stores")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("size").description("페이지 사이즈").optional()
                    ),
                    responseFields(
                        fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                        fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                        fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                        fieldWithPath("stores").type(ARRAY).description("매장 목록"),
                        fieldWithPath("stores[].id").type(NUMBER).description("매장 ID"),
                        fieldWithPath("stores[].name").type(STRING).description("매장 이름"),
                        fieldWithPath("stores[].address").type(STRING).description("매장 주소"),
                        fieldWithPath("stores[].imageUrl").type(STRING).description("매장 이미지 URL")
                    )
                )
            );
    }

    @Test
    @DisplayName("성공: 지역별 매장 목록을 조회한다.")
    void findStores() throws Exception {
        // given
        Region region = 서울;

        int count = 10;
        int page = 0;
        int size = 2;

        List<Store> stores = storesWithId(List.of(), List.of(), count, 서울);
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> pageStores = new PageImpl<>(stores.subList(page * size, size), pageable, count);

        FindStoresResponse response = FindStoresResponse.from(pageStores);

        when(storeService.findStores(pageable, region)).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/stores")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("region", region.name()
                )
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional(),
                        parameterWithName("size").description("페이지 사이즈").optional(),
                        parameterWithName("region").description("지역").optional()
                            .attributes(
                                key("constraints").value(
                                    String.join(", ", Arrays.stream(Region.values()).map(
                                        Region::name).toArray(String[]::new))
                                )
                            )
                    ),
                    responseFields(
                        fieldWithPath("pageInfo").type(OBJECT).description("페이지 정보"),
                        fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 요소 개수"),
                        fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지 여부"),
                        fieldWithPath("stores").type(ARRAY).description("매장 목록"),
                        fieldWithPath("stores[].id").type(NUMBER).description("매장 ID"),
                        fieldWithPath("stores[].name").type(STRING).description("매장 이름"),
                        fieldWithPath("stores[].address").type(STRING).description("매장 주소"),
                        fieldWithPath("stores[].imageUrl").type(STRING).description("매장 이미지 URL"),
                        fieldWithPath("stores[].themes[]").type(ARRAY).description("테마 목록"),
                        fieldWithPath("stores[].songForms[]").type(ARRAY).description("곡 구성 목록")
                    )
                )
            );
    }
}
