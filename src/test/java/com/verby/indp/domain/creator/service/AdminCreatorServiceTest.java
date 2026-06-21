package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creator;
import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.ConflictException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.CreateCreatorRequest;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminCreatorServiceTest {

    @InjectMocks
    private AdminCreatorService adminCreatorService;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private ListeningDailyRepository listeningDailyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("createCreator 메서드 실행 시")
    class CreateCreator {

        @Test
        @DisplayName("성공 : 크리에이터 계정을 생성한다.")
        void createCreator() {
            // given
            given(creatorRepository.existsByEmail("dj@example.com")).willReturn(false);
            given(passwordEncoder.encode("password123!")).willReturn("$2a$hashed");
            CreateCreatorRequest request = new CreateCreatorRequest(
                "박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");

            // when
            Exception exception = catchException(
                () -> adminCreatorService.createCreator(request));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 이메일이면 예외를 던진다.")
        void createCreatorWithDuplicateEmail() {
            // given
            given(creatorRepository.existsByEmail("dj@example.com")).willReturn(true);
            CreateCreatorRequest request = new CreateCreatorRequest(
                "박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");

            // when
            Exception exception = catchException(
                () -> adminCreatorService.createCreator(request));

            // then
            assertThat(exception).isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    @DisplayName("findCreators 메서드 실행 시")
    class FindCreators {

        @Test
        @DisplayName("성공 : 크리에이터 목록을 반환한다.")
        void findCreators() {
            // given
            Creator c1 = creatorWithId(1L);
            Creator c2 = creatorWithId(2L);
            given(creatorRepository.findAll()).willReturn(List.of(c1, c2));
            given(listeningDailyRepository.sumAllSecondsByCreatorId(1L)).willReturn(6912L);
            given(listeningDailyRepository.sumAllSecondsByCreatorId(2L)).willReturn(0L);

            // when
            FindCreatorsResponse response = adminCreatorService.findCreators();

            // then
            assertThat(response.creators()).hasSize(2);
            assertThat(response.creators().get(0).id()).isEqualTo(1L);
            // 6912초 / 3456 = 정확히 2.0원 (1원 단위 내림이 아닌 소수점 적립액)
            assertThat(response.creators().get(0).accruedWon()).isEqualTo(2.0);
            assertThat(response.creators().get(0).totalListenSeconds()).isEqualTo(6912L);
        }
    }

    @Nested
    @DisplayName("deactivate 메서드 실행 시")
    class Deactivate {

        @Test
        @DisplayName("성공 : 크리에이터를 비활성화한다.")
        void deactivate() {
            // given
            Creator c = creatorWithId(1L);
            given(creatorRepository.findById(1L)).willReturn(Optional.of(c));

            // when
            adminCreatorService.deactivate(1L);

            // then
            assertThat(c.isActive()).isFalse();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 크리에이터이면 예외를 던진다.")
        void deactivateWithNotFound() {
            // given
            given(creatorRepository.findById(99L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> adminCreatorService.deactivate(99L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}
