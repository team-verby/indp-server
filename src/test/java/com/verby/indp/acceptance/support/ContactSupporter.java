package com.verby.indp.acceptance.support;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import com.verby.indp.domain.contact.dto.request.RegisterContactRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ContactSupporter {

    public static ExtractableResponse<Response> registerContact(RegisterContactRequest request) {
        return given().log().all()
            .contentType(JSON)
            .accept(JSON)
            .when().log().all()
            .body(request)
            .post("/api/contacts")
            .then().log().all()
            .extract();
    }

}
