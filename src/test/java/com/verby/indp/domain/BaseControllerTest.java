package com.verby.indp.domain;

import static com.verby.indp.global.fixture.TokenFixture.accessToken;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verby.indp.config.RestDocsConfig;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.service.AuthService;
import com.verby.indp.domain.contact.service.ContactService;
import com.verby.indp.domain.recommendation.service.RecommendationService;
import com.verby.indp.domain.region.service.RegionService;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.image.ImageService;
import com.verby.indp.global.jwt.TokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
@Import({RestDocsConfig.class})
public abstract class BaseControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected StoreService storeService;

    @MockBean
    protected ContactService contactService;

    @MockBean
    protected RecommendationService recommendationService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected TokenManager tokenManager;

    @MockBean
    protected AdminRepository adminRepository;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected RegionService regionService;

    protected String accessToken = accessToken();

    @BeforeEach
    void setUp(final WebApplicationContext context,
        final RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(provider))
            .alwaysDo(print())
            .alwaysDo(restDocs)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

}
