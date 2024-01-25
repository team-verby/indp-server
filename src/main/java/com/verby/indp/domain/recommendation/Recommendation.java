package com.verby.indp.domain.recommendation;

import static java.util.Objects.isNull;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.vo.PhoneNumber;
import com.verby.indp.domain.recommendation.vo.RecommendationInformation;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Recommendation extends BaseTimeEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Embedded
    private RecommendationInformation information;

    @Embedded
    private PhoneNumber phoneNumber;

    public Recommendation(
        Store store,
        String information,
        String phoneNumber
    ) {
        validateStore(store);

        this.store = store;
        this.information = new RecommendationInformation(information);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public String getInformation() {
        return information.getInformation();
    }

    public String getPhoneNumber() {
        return phoneNumber.getPhoneNumber();
    }

    private void validateStore(Store store) {
        if (isNull(store)) {
            throw new IllegalArgumentException("존재하지 않는 매장입니다.");
        }
    }
}
