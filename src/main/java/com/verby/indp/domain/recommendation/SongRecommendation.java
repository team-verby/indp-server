package com.verby.indp.domain.recommendation;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Column(name = "referee_name")
    private String refereeName;

    @Column(name = "fee", nullable = false)
    private int fee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public SongRecommendation(Store store, String title, String artist, String vid, String refereeName, int fee) {
        this.store = store;
        this.title = title;
        this.artist = artist;
        this.vid = vid;
        this.refereeName = refereeName;
        this.fee = fee;
    }
}
