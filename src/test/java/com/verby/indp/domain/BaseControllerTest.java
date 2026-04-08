package com.verby.indp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verby.indp.config.RestDocsConfig;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.service.AdminService;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.service.OwnerStoreService;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.service.SubscriptionService;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
    protected AdminRepository adminRepository;

    @MockBean
    protected OwnerRepository ownerRepository;

    @MockBean
    protected AdminService adminService;

    @MockBean
    protected OwnerService ownerService;

    @MockBean
    protected StoreService storeService;

    @MockBean
    protected SongRecommendationService songRecommendationService;

    @MockBean
    protected OwnerStoreService ownerStoreService;

    @MockBean
    protected SubscriptionService subscriptionService;

    @MockBean
    protected PlanService planService;

    @MockBean
    protected PlaylistService playlistService;

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
