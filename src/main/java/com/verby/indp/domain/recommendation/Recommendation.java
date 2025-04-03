package com.verby.indp.domain.recommendation;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.vo.PhoneNumber;
import com.verby.indp.domain.recommendation.vo.RecommendationInformation;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;

@Entity
@Table(name = "recommendation")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Recommendation extends BaseTimeEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
            throw new BadRequestException("존재하지 않는 매장입니다.");
        }
    }
}
