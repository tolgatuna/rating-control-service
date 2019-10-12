package com.duriancodes.ratingcontrolservice.service;

import com.duriancodes.ratingcontrolservice.common.RatingLevels;
import com.duriancodes.ratingcontrolservice.config.RatingControlServiceConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingControlServiceImpl implements RatingControlService {

    private RestTemplate restTemplate;
    private RatingControlServiceConfig serviceConfig;

    public RatingControlServiceImpl(RestTemplate restTemplate, RatingControlServiceConfig serviceConfig) {
        this.restTemplate = restTemplate;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public boolean canReadBook(String customerRatingControlLevel, String bookId) {
        HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
        Integer customerProvidedRatingControlLevel = Integer.valueOf(customerRatingControlLevel);
        ResponseEntity<String> responseEntity = restTemplate.exchange(serviceConfig.getBookServiceEndpoint() + bookId,
                HttpMethod.GET, requestEntity, String.class);

        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Integer bookRatingControlLevel = RatingLevels.RATING_CODE_LEVEL.get(responseEntity.getBody());
            if (containsValidRatingControlLevelCodes(bookRatingControlLevel, customerProvidedRatingControlLevel)) {
                return bookRatingControlLevel <= Integer.parseInt(customerRatingControlLevel);
            }
        }
        return false;
    }

    private MultiValueMap<String, String> getHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private boolean containsValidRatingControlLevelCodes(Integer bookRatingControlLevel, Integer customerProvidedRatingControlLevel) {
        return !StringUtils.isEmpty(bookRatingControlLevel) && !StringUtils.isEmpty(customerProvidedRatingControlLevel);
    }
}
