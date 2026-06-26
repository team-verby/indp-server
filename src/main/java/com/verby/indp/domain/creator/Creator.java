package com.verby.indp.domain.creator;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "creator")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Creator extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dj_name", nullable = false)
    private String djName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "introduction", length = 1000)
    private String introduction;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "is_live", nullable = false)
    private boolean isLive = false;

    @Column(name = "last_live_at")
    private LocalDateTime lastLiveAt;

    /**
     * 스케줄 자동 라이브 대상 여부. 운영용 시드 DJ 계정만 true로 두고,
     * 스케줄러가 라이브 창에 맞춰 isLive/heartbeat를 자동 관리한다. 일반 DJ는 false.
     */
    @Column(name = "auto_live", nullable = false)
    private boolean autoLive = false;

    /** 자동 라이브 시작 시각(LocalTime). null이면 24시간 상시 라이브. */
    @Column(name = "live_start_time")
    private LocalTime liveStartTime;

    /** 자동 라이브 종료 시각(LocalTime). null이면 24시간 상시 라이브. */
    @Column(name = "live_end_time")
    private LocalTime liveEndTime;

    public Creator(String name, String djName, String phone, String email, String password) {
        validateName(name);
        validateDjName(djName);
        validatePhone(phone);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.djName = djName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public void deactivate() {
        this.active = false;
    }

    public void startLive() {
        this.isLive = true;
    }

    public void stopLive() {
        this.isLive = false;
    }

    /**
     * 라이브 하트비트 갱신. 대시보드가 주기적으로 호출해 '실제로 켜져 있음'을 증명한다.
     */
    public void heartbeat(LocalDateTime now) {
        this.lastLiveAt = now;
    }

    /**
     * 실시간 라이브 여부. isLive 플래그가 켜져 있더라도 마지막 하트비트가 TTL을 넘겼으면
     * (탭 강제 종료·크래시 등으로 stopLive가 유실된 경우) 오프라인으로 간주한다.
     */
    public boolean isLiveWithin(LocalDateTime now, long ttlSeconds) {
        return isLive
            && lastLiveAt != null
            && !lastLiveAt.isBefore(now.minusSeconds(ttlSeconds));
    }

    /** 스케줄 자동 라이브를 켠다. start/end가 null이면 24시간 상시 라이브. */
    public void enableAutoLive(LocalTime startTime, LocalTime endTime) {
        this.autoLive = true;
        this.liveStartTime = startTime;
        this.liveEndTime = endTime;
    }

    public void disableAutoLive() {
        this.autoLive = false;
    }

    /**
     * 자동 라이브 대상이고, 현재 시각이 라이브 창 안인지 판단한다.
     * start/end가 null이거나 같으면 24시간 상시 라이브. 자정을 넘는 창(예: 22:00~02:00)도 처리.
     */
    public boolean isWithinLiveWindow(LocalTime now) {
        if (!autoLive) {
            return false;
        }
        if (liveStartTime == null || liveEndTime == null || liveStartTime.equals(liveEndTime)) {
            return true;
        }
        if (liveStartTime.isBefore(liveEndTime)) {
            return !now.isBefore(liveStartTime) && now.isBefore(liveEndTime);
        }
        return !now.isBefore(liveStartTime) || now.isBefore(liveEndTime);
    }

    public void updateProfile(String djName, String thumbnailUrl, String introduction) {
        if (djName != null && !djName.isBlank()) {
            this.djName = djName;
        }
        if (thumbnailUrl != null) {
            this.thumbnailUrl = thumbnailUrl;
        }
        if (introduction != null) {
            validateIntroduction(introduction);
            this.introduction = introduction;
        }
    }

    public void removeThumbnail() {
        this.thumbnailUrl = null;
    }

    private void validateIntroduction(String introduction) {
        if (introduction.length() > 1000) {
            throw new BadRequestException("소개글은 1000자 이하로 작성해 주세요.");
        }
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public boolean mismatchPassword(String rawPassword, PasswordEncoder encoder) {
        return !encoder.matches(rawPassword, this.password);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name은 필수입니다.");
        }
    }

    private void validateDjName(String djName) {
        if (djName == null || djName.isBlank()) {
            throw new BadRequestException("djName은 필수입니다.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new BadRequestException("phone은 필수입니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("email은 필수입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("password는 필수입니다.");
        }
    }
}
