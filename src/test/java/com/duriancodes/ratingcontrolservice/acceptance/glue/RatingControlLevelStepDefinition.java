package com.duriancodes.ratingcontrolservice.acceptance.glue;

import com.duriancodes.ratingcontrolservice.RatingControlServiceApiApplication;
import com.duriancodes.ratingcontrolservice.acceptance.fixtures.BookServiceFixture;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java8.En;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@AutoConfigureWireMock(port = 9999)
@SpringBootTest(classes = RatingControlServiceApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RatingControlLevelStepDefinition implements En {

    private String customerSetControlLevel;
    private Response response;

    public RatingControlLevelStepDefinition() {
        Given("^I am customer who have set rating control level (.*)$", (String customerControlLevel) -> {
            this.customerSetControlLevel = customerControlLevel;
        });

        When("^I request to read equal level book (.*)$", (String bookId) -> {
            BookServiceFixture.stubBookServiceResponseForBook_B1234_Rating12(bookId);
            response = given()
                    .headers(generateHeader())
                    .when()
                    .get("/rating-test-control/rcl/book/v1/read/eligibility/" + this.customerSetControlLevel + "/" + bookId);
        });

        Then("^I get decision to read the book$", () -> {
            WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/fetch/book/rating/B1234")));
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(Boolean.TRUE.toString()));
        });

        When("I request to read higher level book (.*)$", (String bookId) -> {
            BookServiceFixture.stubBookServiceResponseForBook_BH1234_Rating15(bookId);
            response = given()
                    .headers(generateHeader())
                    .when()
                    .get("/rating-test-control/rcl/book/v1/read/eligibility/" + this.customerSetControlLevel + "/" + bookId);
        });

        Then("I get decision not to read the book", () -> {
            WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/fetch/book/rating/BH1234")));
            response.then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(Boolean.FALSE.toString()));
        });
    }

    private Headers generateHeader() {
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Accept", "application/json"));
        return new Headers(headers);
    }
}
