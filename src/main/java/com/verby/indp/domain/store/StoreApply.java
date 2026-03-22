package com.verby.indp.domain.store;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store_apply")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreApply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_apply_id")
    private Long storeApplyId;

    @Column(name = "applicant_name")
    private String applicantName;

    @Column(name = "applicant_phone")
    private String applicantPhone;

    @Column(name = "inquiry_content", columnDefinition = "TEXT")
    private String inquiryContent;

    public StoreApply(String applicantName, String applicantPhone, String inquiryContent) {
        this.applicantName = applicantName;
        this.applicantPhone = applicantPhone;
        this.inquiryContent = inquiryContent;
    }
}
