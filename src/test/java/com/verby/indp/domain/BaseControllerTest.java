package com.verby.indp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verby.indp.config.RestDocsConfig;
import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.auth.service.AdminService;
import com.verby.indp.domain.auth.service.AuthTokenService;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.auth.service.UnifiedAuthService;
import com.verby.indp.domain.auth.service.UserService;
import com.verby.indp.domain.auth.service.AdminUserService;
import com.verby.indp.domain.auth.service.UserApplicationService;
import com.verby.indp.domain.auth.service.UserSubscriptionService;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.service.AdminCreatorService;
import com.verby.indp.domain.creator.service.DjLiveService;
import com.verby.indp.domain.creator.service.DjPlaylistService;
import com.verby.indp.domain.creator.service.DjRevenueService;
import com.verby.indp.domain.creator.service.DjService;
import com.verby.indp.domain.creator.service.DjTrackService;
import com.verby.indp.domain.listening.service.ListeningService;
import com.verby.indp.domain.payment.service.AdminPaymentService;
import com.verby.indp.domain.payment.service.OwnerPaymentService;
import com.verby.indp.domain.payment.service.PaymentConfirmService;
import com.verby.indp.domain.payment.service.PaymentService;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.playlist.service.AdminFixedPlaylistSongService;
import com.verby.indp.domain.playlist.service.AdminMoodScheduleService;
import com.verby.indp.domain.playlist.service.AdminMusicCatalogService;
import com.verby.indp.domain.playlist.service.AdminPlaylistService;
import com.verby.indp.domain.playlist.service.OwnerPlaylistService;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.playlist.service.StoreSseService;
import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.service.AdminStoreService;
import com.verby.indp.domain.store.service.ApplyStoreService;
import com.verby.indp.domain.store.service.OwnerStoreService;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.image.ImageService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
@Import({RestDocsConfig.class})
public abstract class BaseControllerTest {

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String BEARER_TOKEN = "Bearer test-token";

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected AuthTokenService authTokenService;

    @MockBean
    protected AdminRepository adminRepository;

    @MockBean
    protected OwnerRepository ownerRepository;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected CreatorRepository creatorRepository;

    @MockBean
    protected AdminService adminService;

    @MockBean
    protected OwnerService ownerService;

    @MockBean
    protected UnifiedAuthService unifiedAuthService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected AdminCreatorService adminCreatorService;

    @MockBean
    protected PaymentService paymentService;

    @MockBean
    protected PaymentConfirmService paymentConfirmService;

    @MockBean
    protected AdminPaymentService adminPaymentService;

    @MockBean
    protected OwnerPaymentService ownerPaymentService;

    @MockBean
    protected PlanService planService;

    @MockBean
    protected PlaylistService playlistService;

    @MockBean
    protected OwnerPlaylistService ownerPlaylistService;

    @MockBean
    protected AdminPlaylistService adminPlaylistService;

    @MockBean
    protected AdminFixedPlaylistSongService fixedPlaylistSongService;

    @MockBean
    protected AdminMusicCatalogService musicCatalogService;

    @MockBean
    protected AdminMoodScheduleService moodScheduleService;

    @MockBean
    protected SongRecommendationService songRecommendationService;

    @MockBean
    protected PricePolicyService pricePolicyService;

    @MockBean
    protected StoreService storeService;

    @MockBean
    protected ApplyStoreService applyStoreService;

    @MockBean
    protected OwnerStoreService ownerStoreService;

    @MockBean
    protected AdminStoreService adminStoreService;

    @MockBean
    protected SubscriptionService subscriptionService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected StoreSseService storeSseService;

    @MockBean
    protected DjService djService;

    @MockBean
    protected DjTrackService djTrackService;

    @MockBean
    protected DjLiveService djLiveService;

    @MockBean
    protected DjPlaylistService djPlaylistService;

    @MockBean
    protected DjRevenueService djRevenueService;

    @MockBean
    protected UserSubscriptionService userSubscriptionService;

    @MockBean
    protected UserApplicationService userApplicationService;

    @MockBean
    protected AdminUserService adminUserService;

    @MockBean
    protected ListeningService listeningService;

    @MockBean
    protected com.verby.indp.domain.settlement.service.AdminSettlementService adminSettlementService;

    @MockBean
    protected com.verby.indp.global.seed.SeedService seedService;

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

    protected void givenOwnerAuth(Owner owner) {
        given(authTokenService.decodeOwnerToken(anyString())).willReturn(owner.getOwnerId());
        given(ownerRepository.findById(owner.getOwnerId())).willReturn(Optional.of(owner));
    }

    protected void givenAdminAuth(Admin admin) {
        Long adminId = admin.getAdminId();
        given(authTokenService.decodeAdminToken(anyString())).willReturn(adminId);
        given(adminRepository.findById(adminId)).willReturn(Optional.of(admin));
    }

    protected void givenCreatorAuth(Creator creator) {
        Long creatorId = creator.getCreatorId();
        given(authTokenService.decodeCreatorToken(anyString())).willReturn(creatorId);
        given(creatorRepository.findById(creatorId)).willReturn(Optional.of(creator));
    }

    protected void givenUserAuth(com.verby.indp.domain.auth.User user) {
        Long userId = user.getUserId();
        given(authTokenService.decodeUserToken(anyString())).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
    }
}
