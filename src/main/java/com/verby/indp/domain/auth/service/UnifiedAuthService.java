package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.UnifiedLoginResponse;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnifiedAuthService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final CreatorRepository creatorRepository;
    private final StoreRepository storeRepository;
    private final AuthTokenService authTokenService;

    @Transactional
    public UnifiedLoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByLoginId(request.loginId());
        if (userOpt.isPresent()) {
            return loginAsUser(userOpt.get(), request.password());
        }

        Optional<Creator> creatorOpt = creatorRepository.findByEmail(request.loginId());
        if (creatorOpt.isPresent()) {
            return loginAsCreator(creatorOpt.get(), request.password());
        }

        Optional<Owner> ownerOpt = ownerRepository.findByLoginId(request.loginId());
        if (ownerOpt.isPresent()) {
            return loginAsOwner(ownerOpt.get(), request.password());
        }

        throw new AuthException("존재하지 않는 계정입니다.");
    }

    private UnifiedLoginResponse loginAsUser(User user, String password) {
        if (user.mismatchPassword(password)) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = authTokenService.createUserToken(user.getUserId());
        RefreshToken refreshToken = authTokenService.issueUserRefreshToken(user.getUserId());
        return new UnifiedLoginResponse(accessToken, refreshToken.getToken(), "PLAN_A", null, null);
    }

    private UnifiedLoginResponse loginAsCreator(Creator creator, String password) {
        if (!creator.isActive()) {
            throw new AuthException("비활성화된 계정입니다.");
        }
        if (creator.mismatchPassword(password)) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = authTokenService.createCreatorToken(creator.getCreatorId());
        RefreshToken refreshToken = authTokenService.issueCreatorRefreshToken(creator.getCreatorId());
        return new UnifiedLoginResponse(accessToken, refreshToken.getToken(), "DJ", null, creator.getDjName());
    }

    private UnifiedLoginResponse loginAsOwner(Owner owner, String password) {
        if (owner.mismatchPassword(password)) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = authTokenService.createOwnerToken(owner.getOwnerId());
        RefreshToken refreshToken = authTokenService.issueOwnerRefreshToken(owner.getOwnerId());

        List<Store> stores = storeRepository.findAllByOwner(owner);
        Long storeId = stores.isEmpty() ? null : stores.get(0).getStoreId();
        String planType = resolvePlanType(stores);

        return new UnifiedLoginResponse(accessToken, refreshToken.getToken(), planType, storeId, null);
    }

    private String resolvePlanType(List<Store> stores) {
        return stores.stream()
            .flatMap(store -> store.getSubscriptions().stream())
            .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
            .map(StoreSubscription::getPlan)
            .map(Plan::getType)
            .map(Plan.PlanType::name)
            .findFirst()
            .orElse("PLAN_B");
    }
}
