package com.verby.indp.domain.region.controller;

import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.store.dto.response.FindRegionsResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class RegionControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공 : 지역 목록을 조회한다.")
    void findRegions() throws Exception {
        // given
        List<Region> regions = List.of(region("서울"), region("경기"));
        FindRegionsResponse response = FindRegionsResponse.from(regions);

        when(regionService.findRegions()).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/regions")
        );

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("regions").type(ARRAY).description("매장 지역 목록")
                    )
                )
            );
    }
}
