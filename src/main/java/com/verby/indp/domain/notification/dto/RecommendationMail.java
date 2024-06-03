package com.verby.indp.domain.notification.dto;

public record RecommendationMail(
    String to,
    String information,
    String phoneNumber,
    String storeName,
    String storeAddress
) {
    public static RecommendationMail of(
        String to,
        String information,
        String phoneNumber,
        String storeName,
        String storeAddress
    ) {
        return new RecommendationMail(to, information, phoneNumber, storeName, storeAddress);
    }

}
