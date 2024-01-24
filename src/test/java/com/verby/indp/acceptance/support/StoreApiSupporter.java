package com.verby.indp.acceptance.support;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StoreApiSupporter {

    public static ExtractableResponse<Response> findSimpleStores(int page, int size) {
        return given().log().all()
            .accept(JSON)
            .when().log().all()
            .queryParam("size", size)
            .queryParam("page", page)
            .get("/api/main/stores")
            .then().log().all()
            .extract();
    }

}
