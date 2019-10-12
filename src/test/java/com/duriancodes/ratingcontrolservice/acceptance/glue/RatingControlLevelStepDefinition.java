package com.duriancodes.ratingcontrolservice.acceptance.glue;

import com.duriancodes.ratingcontrolservice.RatingControlServiceApiApplication;
import io.cucumber.java8.En;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RatingControlServiceApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RatingControlLevelStepDefinition implements En {

    private String customerSetControlLevel;
    private ResponseEntity<Boolean> responseEntity;

    @Autowired
    private TestRestTemplate restTemplate;

    public RatingControlLevelStepDefinition() {
        Given("^I am customer who have set rating control level (.*)$", (String customerControlLevel) -> {
            this.customerSetControlLevel = customerControlLevel;
        });

        When("^I request to read equal level book (.*)$", (String bookId) -> {
            HttpEntity httpEntity = new HttpEntity(generateHeader());
            responseEntity = (ResponseEntity<Boolean>) given().headers(generateHeader())
                    .when()
                    .get("/rcl/book/v1/read/eligibility/" + this.customerSetControlLevel + "/" + bookId);
        });

        Then("^I get decision to read the book$", () -> {
            assertThat("responseEntity status code is not 200", responseEntity.getStatusCode(), is(200));
            assertThat("responseEntity body is not true", responseEntity.getBody(), is(Boolean.TRUE));
        });
    }

    private MultiValueMap<String, String> generateHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        return headers;
    }
}
