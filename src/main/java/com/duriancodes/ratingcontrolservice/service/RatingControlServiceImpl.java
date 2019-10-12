package com.duriancodes.ratingcontrolservice.service;

import com.duriancodes.ratingcontrolservice.service.RatingControlService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingControlServiceImpl implements RatingControlService {

    private static final String THIRD_PARTY_URL = "https://my-third-party.service.com/fetch/book/";

    private RestTemplate restTemplate;

    public RatingControlServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean canReadBook(String customerRatingControlLevel, String bookId) {
        HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(THIRD_PARTY_URL + bookId,
                HttpMethod.GET, requestEntity, String.class);

        if (HttpStatus.OK == responseEntity.getStatusCode()) {
            String ratingControlLevel = responseEntity.getBody();
            return Integer.parseInt(ratingControlLevel) <= Integer.parseInt(customerRatingControlLevel);
        }
        return false;
    }

    private MultiValueMap<String, String> getHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
