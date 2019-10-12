package com.duriancodes.ratingcontrolservice.service;

import com.duriancodes.ratingcontrolservice.common.RatingLevels;
import com.duriancodes.ratingcontrolservice.config.RatingControlServiceConfig;
import com.duriancodes.ratingcontrolservice.exception.BookNotFoundException;
import com.duriancodes.ratingcontrolservice.exception.TechnicalFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
        Map<String, Integer> ratingCodeLevelMap = RatingLevels.RATING_CODE_LEVEL;
        HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
        Integer customerProvidedRatingControlLevel = ratingCodeLevelMap.get(customerRatingControlLevel);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(serviceConfig.getBookServiceEndpoint() + bookId,
                    HttpMethod.GET, requestEntity, String.class);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                Integer bookRatingControlLevel = ratingCodeLevelMap.get(responseEntity.getBody());
                if (containsValidRatingControlLevelCodes(bookRatingControlLevel, customerProvidedRatingControlLevel)) {
                    return bookRatingControlLevel <= customerProvidedRatingControlLevel;
                }
            }
            return false;
        } catch (TechnicalFailureException ex) {
            return false;
        } catch (BookNotFoundException notFoundEx) {
            throw new BookNotFoundException(String.format("Book not found for : {}", bookId));
        }
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
