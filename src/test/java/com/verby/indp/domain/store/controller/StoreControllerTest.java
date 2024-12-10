package com.verby.indp.domain.store.controller;

import static com.verby.indp.domain.auth.fixture.AdminFixture.admin;
import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static com.verby.indp.domain.store.fixture.StoreFixture.storesWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.AddStoreByAdminRequest;
import com.verby.indp.domain.store.dto.request.UpdateStoreByAdminRequest;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class StoreControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 매장 목록을 조회한다.(간단 정보)")
    void findSimpleStores() throws Exception {
        // given
        int count = 10;
        int page = 0;
        int size = 2;

        Region 서울 = region("서울");
        List<Store> stores = storesWithId(서울, List.of(), List.of(), count);
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
        Region 서울  = region("서울");

        int count = 10;
        int page = 0;
        int size = 2;

        List<Store> stores = storesWithId(List.of(), List.of(), count, 서울);
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> pageStores = new PageImpl<>(stores.subList(page * size, size), pageable, count);

        FindStoresResponse response = FindStoresResponse.from(pageStores);

        when(storeService.findStores(pageable, 서울.getRegion())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/stores")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("region", 서울.getRegion()
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

    @Test
    @DisplayName("성공 : (관리자) 매장 목록을 조회한다.")
    void findStoresByAdmin() throws Exception {
        // given
        com.verby.indp.domain.region.Region 서울  = region("서울");

        int count = 10;
        int page = 0;
        int size = 2;

        List<Store> stores = storesWithId(List.of(), List.of(), count, 서울);
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> pageStores = new PageImpl<>(stores.subList(page * size, size), pageable, count);

        FindStoresByAdminResponse response = FindStoresByAdminResponse.from(pageStores);

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));
        when(storeService.findStoresByAdmin(pageable)).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/admin/stores")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .header("Authorization", "Bearer " + accessToken)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
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
                        fieldWithPath("stores[].region").type(STRING).description("매장 지역"),
                        fieldWithPath("stores[].imageUrl").type(STRING).description("매장 이미지 URL"),
                        fieldWithPath("stores[].themes[]").type(ARRAY).description("테마 목록"),
                        fieldWithPath("stores[].songForms[]").type(ARRAY).description("곡 구성 목록")
                    )
                )
            );
    }

    @Test
    @DisplayName("성공 : (관리자) 매장을 조회한다.")
    void findStoreByAdmin() throws Exception {
        // given
        Region 서울 = region("서울");
        Store store = store(서울);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        FindStoreByAdminResponse response = FindStoreByAdminResponse.from(store);

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));
        when(storeService.findStoreByAdmin(store.getStoreId())).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/admin/stores/{storeId}", store.getStoreId())
                .header(AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("storeId").description("매장 ID")
                    ),
                    responseFields(
                        fieldWithPath("name").type(STRING).description("매장 이름"),
                        fieldWithPath("address").type(STRING).description("매장 주소"),
                        fieldWithPath("imageUrl").type(STRING).description("매장 이미지 URL"),
                        fieldWithPath("region").type(STRING).description("매장 지역"),
                        fieldWithPath("themes[]").type(ARRAY).description("테마 목록"),
                        fieldWithPath("songForms[]").type(ARRAY).description("곡 구성 목록")
                    )
                )
            );
    }

    @Test
    @DisplayName("성공 : (관리자) 매장을 삭제한다.")
    void removeStoreByAdmin() throws Exception {
        // given
        Region 서울 = region("서울");
        Store store = store(서울);
        ReflectionTestUtils.setField(store, "storeId", 1L);

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/admin/stores/{storeId}", store.getStoreId())
                .header(AUTHORIZATION, "Bearer " + accessToken)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("storeId").description("매장 ID")
                    )
                )
            );

    }

    @Test
    @DisplayName("성공 : (관리자) 매장을 추가한다.")
    void addStoreByAdmin() throws Exception {
        // given
        Region 서울 = region("서울");
        Store store = store(서울);
        AddStoreByAdminRequest request = new AddStoreByAdminRequest(store.getName(),
            store.getAddress(), store.getRegion(),
            store.getImage().get(0), store.getThemes(), store.getSongForms());

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/admin/stores")
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));

        // then
        resultActions.andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("매장 이름"),
                        fieldWithPath("address").type(STRING).description("매장 주소"),
                        fieldWithPath("imageUrl").type(STRING).description("매장 이미지 URL"),
                        fieldWithPath("region").type(STRING).description("매장 지역"),
                        fieldWithPath("themes[]").type(ARRAY).description("테마 목록"),
                        fieldWithPath("songForms[]").type(ARRAY).description("곡 구성 목록")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("생성된 리소스 접근 API")
                    )
                )
            );
    }

    @Test
    @DisplayName("성공 : (관리자) 매장 정보를 수정한다.")
    void updateStoreByAdmin() throws Exception {
        // given
        Region 서울 = region("서울");
        Store store = store(서울);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        UpdateStoreByAdminRequest request = new UpdateStoreByAdminRequest(
            store.getName(), store.getAddress(), store.getRegion(),
            store.getImage().get(0), store.getThemes(), store.getSongForms());

        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));

        // when
        ResultActions resultActions = mockMvc.perform(
            put("/api/admin/stores/{storeId}", store.getStoreId())
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName(AUTHORIZATION).description("액세스 토큰")
                    ),
                    pathParameters(
                        parameterWithName("storeId").description("매장 ID")
                    ),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("매장 이름"),
                        fieldWithPath("address").type(STRING).description("매장 주소"),
                        fieldWithPath("imageUrl").type(STRING).description("매장 이미지 URL"),
                        fieldWithPath("region").type(STRING).description("매장 지역"),
                        fieldWithPath("themes[]").type(ARRAY).description("테마 목록"),
                        fieldWithPath("songForms[]").type(ARRAY).description("곡 구성 목록")
                    )
                )
            );
    }
}
