package com.verby.indp.domain.recommendation;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
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

    public SongRecommendation(Store store, String title, String artist, String vid, Integer playTime, String refereeName, Payment payment) {
        this.store = store;
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.playTime = playTime;
        this.refereeName = refereeName;
        this.payment = payment;
    }

    public void updateStatus(RecommendationStatus status) {
        this.status = status;
    }
}
