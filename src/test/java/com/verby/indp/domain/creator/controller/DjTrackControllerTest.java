package com.verby.indp.domain.creator.controller;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static com.verby.indp.fixture.CreatorTrackFixture.trackWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.verby.indp.domain.BaseControllerTest;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import com.verby.indp.domain.creator.dto.request.DjTrackUploadUrlRequest;
import com.verby.indp.domain.creator.dto.request.RegisterDjTrackRequest;
import com.verby.indp.domain.creator.dto.response.DjTrackResponse;
import com.verby.indp.domain.creator.dto.response.DjTrackUploadUrlResponse;
import com.verby.indp.domain.creator.dto.response.FindDjTracksResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

class DjTrackControllerTest extends BaseControllerTest {

    @Nested
    @DisplayName("GET /api/dj/tracks 실행 시")
    class GetTracks {

        @Test
        @DisplayName("성공 : 트랙 목록을 반환한다.")
        void getTracks() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            CreatorTrack track = trackWithId(creator, 1L);
            given(djTrackService.getTracks(any()))
                .willReturn(new FindDjTracksResponse(List.of(DjTrackResponse.from(track))));

            ResultActions resultActions = mockMvc.perform(get("/api/dj/tracks")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    responseFields(
                        fieldWithPath("tracks").type(ARRAY).description("트랙 목록"),
                        fieldWithPath("tracks[].id").type(NUMBER).description("트랙 ID"),
                        fieldWithPath("tracks[].filename").type(STRING).description("파일명"),
                        fieldWithPath("tracks[].duration").type(STRING).description("재생 시간 (예: 3:42)"),
                        fieldWithPath("tracks[].secs").type(NUMBER).description("재생 시간(초)"),
                        fieldWithPath("tracks[].streamUrl").type(STRING).description("스트리밍 URL")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("POST /api/dj/tracks 실행 시")
    class UploadTrack {

        @Test
        @DisplayName("성공 : 트랙을 업로드한다.")
        void uploadTrack() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            CreatorTrack track = trackWithId(creator, 1L);
            given(djTrackService.uploadTrack(any(), any(), anyString(), anyInt()))
                .willReturn(DjTrackResponse.from(track));

            MockMultipartFile file = new MockMultipartFile(
                "file", "track.mp3", "audio/mpeg", new byte[]{1, 2, 3});

            ResultActions resultActions = mockMvc.perform(multipart("/api/dj/tracks")
                .file(file)
                .param("duration", "3:42")
                .param("secs", "222")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA));

            resultActions.andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("POST /api/dj/tracks/upload-url 실행 시")
    class CreateUploadUrl {

        @Test
        @DisplayName("성공 : presigned 업로드 URL을 반환한다.")
        void createUploadUrl() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            given(djTrackService.createUploadUrl(any()))
                .willReturn(new DjTrackUploadUrlResponse(
                    "https://s3.example.com/audio/uuid-track.mp3?signature=x",
                    "https://cdn.example.com/audio/uuid-track.mp3"));

            DjTrackUploadUrlRequest request = new DjTrackUploadUrlRequest("track.mp3");

            ResultActions resultActions = mockMvc.perform(post("/api/dj/tracks/upload-url")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("filename").type(STRING).description("업로드할 파일명")
                    ),
                    responseFields(
                        fieldWithPath("uploadUrl").type(STRING).description("S3 직접 업로드용 presigned PUT URL"),
                        fieldWithPath("streamUrl").type(STRING).description("업로드 후 접근할 스트리밍 URL")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("POST /api/dj/tracks/register 실행 시")
    class RegisterTrack {

        @Test
        @DisplayName("성공 : S3 직접 업로드된 트랙을 등록한다.")
        void registerTrack() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            CreatorTrack track = trackWithId(creator, 1L);
            given(djTrackService.registerTrack(any(), any()))
                .willReturn(DjTrackResponse.from(track));

            RegisterDjTrackRequest request = new RegisterDjTrackRequest(
                "track.mp3", "https://cdn.example.com/audio/track.mp3", "3:42", 222);

            ResultActions resultActions = mockMvc.perform(post("/api/dj/tracks/register")
                .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            resultActions.andExpect(status().isCreated())
                .andDo(restDocs.document(
                    requestFields(
                        fieldWithPath("filename").type(STRING).description("파일명"),
                        fieldWithPath("streamUrl").type(STRING).description("S3 업로드 완료 후 받은 스트리밍 URL"),
                        fieldWithPath("duration").type(STRING).description("재생 시간 (예: 3:42)"),
                        fieldWithPath("secs").type(NUMBER).description("재생 시간(초)")
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("트랙 ID"),
                        fieldWithPath("filename").type(STRING).description("파일명"),
                        fieldWithPath("duration").type(STRING).description("재생 시간 (예: 3:42)"),
                        fieldWithPath("secs").type(NUMBER).description("재생 시간(초)"),
                        fieldWithPath("streamUrl").type(STRING).description("스트리밍 URL")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("DELETE /api/dj/tracks/{trackId} 실행 시")
    class DeleteTrack {

        @Test
        @DisplayName("성공 : 트랙을 삭제한다.")
        void deleteTrack() throws Exception {
            Creator creator = creatorWithId(1L);
            givenCreatorAuth(creator);
            willDoNothing().given(djTrackService).deleteTrack(any(), any(Long.class));

            ResultActions resultActions = mockMvc.perform(
                delete("/api/dj/tracks/{trackId}", 1L)
                    .header(AUTHORIZATION_HEADER, BEARER_TOKEN));

            resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                    pathParameters(parameterWithName("trackId").description("트랙 ID"))
                ));
        }
    }
}
