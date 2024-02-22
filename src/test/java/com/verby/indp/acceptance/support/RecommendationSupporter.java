package com.verby.indp.acceptance.support;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RecommendationSupporter {

    public static ExtractableResponse<Response> registerRecommendation(
        RegisterRecommendationRequest request) {
        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .when().log().all()
            .body(request)
            .post("/api/music/recommendations")
            .then().log().all()
            .extract();
    }

}
