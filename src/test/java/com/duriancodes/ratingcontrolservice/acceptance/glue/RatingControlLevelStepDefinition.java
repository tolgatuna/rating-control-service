package com.duriancodes.ratingcontrolservice.acceptance.glue;

import com.duriancodes.ratingcontrolservice.RatingControlServiceApiApplication;
import com.duriancodes.ratingcontrolservice.acceptance.fixtures.BookServiceFixture;
import io.cucumber.java8.En;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@AutoConfigureWireMock(port = 9999)
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
            BookServiceFixture.stubBookServiceResponseForBook_B1234_Rating12(bookId);
            HttpEntity httpEntity = new HttpEntity(generateHeader());
            responseEntity = restTemplate.exchange("/rcl/book/v1/read/eligibility/" + this.customerSetControlLevel + "/" + bookId,
                    HttpMethod.GET, httpEntity, Boolean.class);
        });

        Then("^I get decision to read the book$", () -> {
            assertThat("responseEntity status code is not 200", responseEntity.getStatusCode(), is(HttpStatus.OK));
            assertThat("responseEntity body is not true", responseEntity.getBody(), is(Boolean.TRUE));
        });

        When("I request to read higher level book (.*)$", (String bookId) -> {
            BookServiceFixture.stubBookServiceResponseForBook_BH1234_Rating15(bookId);
            HttpEntity httpEntity = new HttpEntity(generateHeader());
            responseEntity = restTemplate.exchange("/rcl/book/v1/read/eligibility/" + this.customerSetControlLevel + "/" + bookId,
                    HttpMethod.GET, httpEntity, Boolean.class);
        });

        Then("I get decision not to read the book", () -> {
            Assert.assertThat("responseEntity status code is not 200", responseEntity.getStatusCodeValue(), is(200));
            Assert.assertThat("Book Read Eligibility is true", responseEntity.getBody(), is(Boolean.FALSE));
        });
    }

    private MultiValueMap<String, String> generateHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        return headers;
    }
}
