package com.verby.indp.global.image;

import static com.verby.indp.domain.auth.fixture.AdminFixture.admin;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.global.image.dto.ImageResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

class ImageControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공 : 이미지를 업로드한다.")
    void uploadImage() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("image", "image.png", IMAGE_PNG_VALUE, "content".getBytes());
        ImageResponse response = new ImageResponse("imageUrl");

        when(imageService.uploadImage(any())).thenReturn(response.imageUrl());
        when(adminRepository.findById(any())).thenReturn(Optional.of(admin()));

        // when
        ResultActions resultActions = mockMvc.perform(
            multipart("/api/admin/images")
                .file("image", file.getBytes())
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;})
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestParts(
                        partWithName("image").description("이미지 파일")
                    ),
                    responseFields(
                        fieldWithPath("imageUrl").type(STRING).description("이미지 url")
                    )
                )
            );
    }

}
