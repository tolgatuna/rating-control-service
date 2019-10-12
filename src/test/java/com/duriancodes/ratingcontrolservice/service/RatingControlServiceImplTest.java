package com.duriancodes.ratingcontrolservice.service;

import com.duriancodes.ratingcontrolservice.config.RatingControlServiceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RatingControlServiceImplTest {
    private static final String VALID_URL_BOOK_SERVICE = "https://my-third-party.service.com/fetch/book/rating/";
    private static final String CUSTOMER_RATING_LEVEL_CODE_U = "U";
    private static final String CUSTOMER_RATING_LEVEL_CODE_8 = "8";
    private static final String CUSTOMER_RATING_LEVEL_CODE_12 = "12";
    private static final String CUSTOMER_RATING_LEVEL_CODE_15 = "15";
    private static final String CUSTOMER_RATING_LEVEL_CODE_18 = "18";
    private static final String TEST_BOOK_ID = "M1211";
    private static final String TEST_SAMPLE_BOOK_ID = "S1211";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_U = "U";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_8 = "8";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_12 = "12";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_15 = "15";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_18 = "18";
    private static final String BOOK_SERVICE_RATING_LEVEL_CODE_XX = "XX";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RatingControlServiceConfig ratingControlServiceConfig;
    private RatingControlServiceImpl ratingControlService;

    @Before
    public void setUp() throws Exception {
        when(ratingControlServiceConfig.getBookServiceEndpoint()).thenReturn(VALID_URL_BOOK_SERVICE);
        ratingControlService = new RatingControlServiceImpl(restTemplate, ratingControlServiceConfig);
    }

    @Test
    public void shouldReturnTrue_whenBookCodeLevelReturnAs12_andCustomerProvidedRatingCodeIs12() {
        when(restTemplate.exchange(anyString(), (HttpMethod) any(), (HttpEntity) any(), Mockito.<Class<String>>any()))
                .thenReturn(new ResponseEntity<>(BOOK_SERVICE_RATING_LEVEL_CODE_12, HttpStatus.OK));

        assertTrue("Read book eligibility is false", ratingControlService.canReadBook(CUSTOMER_RATING_LEVEL_CODE_12, TEST_BOOK_ID));
    }
}