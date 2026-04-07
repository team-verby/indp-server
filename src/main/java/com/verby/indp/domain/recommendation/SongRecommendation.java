package com.verby.indp.domain.recommendation;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "song_recommendation")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SongRecommendation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_recommendation_id")
    private Long songRecommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "vid")
    private String vid;

    @Column(name = "play_time")
    private Integer playTime;

    @Column(name = "referee_name")
    private String refereeName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecommendationStatus status = RecommendationStatus.PENDING_PAYMENT;

    public enum RecommendationStatus {
        PENDING_PAYMENT, RECOMMENDED, PAYMENT_FAILED
    }

    public SongRecommendation(Store store, String title, String artist, String vid,
        Integer playTime, String refereeName, Payment payment) {
        validateStore(store);
        validateTitle(title);
        validateArtist(artist);
        validateVid(vid);
        validatePlayTime(playTime);
        validateRefereeName(refereeName);
        validatePayment(payment);
        this.store = store;
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.playTime = playTime;
        this.refereeName = refereeName;
        this.payment = payment;
    }

    public void updateStatus(RecommendationStatus status) {
        validateStatus(status);
        this.status = status;
    }

    private void validateStore(Store store) {
        if (store == null) {
            throw new BadRequestException("store는 필수입니다.");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("title은 필수입니다.");
        }
    }

    private void validateArtist(String artist) {
        if (artist == null || artist.isBlank()) {
            throw new BadRequestException("artist는 필수입니다.");
        }
    }

    private void validateVid(String vid) {
        if (vid == null || vid.isBlank()) {
            throw new BadRequestException("vid는 필수입니다.");
        }
    }

    private void validatePlayTime(Integer playTime) {
        if (playTime != null && playTime <= 0) {
            throw new BadRequestException("playTime은 양수여야 합니다.");
        }
    }

    private void validateRefereeName(String refereeName) {
        if (refereeName == null || refereeName.isBlank()) {
            throw new BadRequestException("refereeName은 필수입니다.");
        }
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new BadRequestException("payment는 필수입니다.");
        }
    }

    private void validateStatus(RecommendationStatus status) {
        if (status == null) {
            throw new BadRequestException("status는 필수입니다.");
        }
    }
}
