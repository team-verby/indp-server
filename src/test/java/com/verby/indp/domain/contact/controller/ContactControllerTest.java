package com.verby.indp.domain.contact.controller;

import static com.verby.indp.domain.contact.fixture.ContactFixture.contact;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.contact.Contact;
import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class ContactControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: 문의 사항을 등록한다.")
    void registerContact() throws Exception {
        // given
        Contact contact = contact();

        RegisterContactRequest request = new RegisterContactRequest(contact.getUserName(),
            contact.getContent(), contact.getPhoneNumber());

        when(contactService.registerContact(request)).thenReturn(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/contacts")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("userName").type(STRING).description("문의자 성함"),
                        fieldWithPath("content").type(STRING).description("문의 내용"),
                        fieldWithPath("phoneNumber").type(STRING).description("문의자 연락처")
                    ),
                    responseHeaders(
                        headerWithName("Location").description("리소스 생성 위치")
                    )
                )
            );
    }

}
