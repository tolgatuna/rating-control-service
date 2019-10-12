package com.duriancodes.ratingcontrolservice.controller;

import com.tdd.book.swagger.api.RlcApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingControlLevelController implements RlcApi {
    private static final String ALPHABET_REGEX = "[a-zA-Z]+";
    private static final String NUMBER_REGEX = "[0-9]+";
    private static final String NO_SPECIAL_CHAR_REGEX = "[a-zA-Z0-9]*";

    @Override
    public ResponseEntity<Boolean> getControlAccess(String controlLevel, String bookId) {
        if (isValidControlLevel(controlLevel) || containsSpecialCharacters(bookId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public boolean isValidControlLevel(String controlLevel) {
        return controlLevel.matches(ALPHABET_REGEX) || controlLevel.matches(NUMBER_REGEX);
    }

    public boolean containsSpecialCharacters(String bookId) {
        return !bookId.matches(NO_SPECIAL_CHAR_REGEX);
    }
}
