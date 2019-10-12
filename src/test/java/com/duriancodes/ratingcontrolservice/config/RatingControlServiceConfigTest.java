package com.duriancodes.ratingcontrolservice.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RatingControlServiceConfigTest {
    private static final String BOOK_SERVICE_URL = "https://my-third-party.service.com/fetch/book/rating/";

    @Autowired
    private RatingControlServiceConfig ratingControlServiceConfig;

    @Test
    public void loadContext() {
    }

    @Test
    public void shouldLoadBookServiceEndPoint() {
        assertNotNull("Book Service Endpoint value is null", ratingControlServiceConfig.getBookServiceEndpoint());
        assertEquals("Book Service Endpoint value mismatch", ratingControlServiceConfig.getBookServiceEndpoint(), BOOK_SERVICE_URL);
    }
}