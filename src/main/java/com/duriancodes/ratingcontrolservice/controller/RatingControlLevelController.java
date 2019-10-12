package com.duriancodes.ratingcontrolservice.controller;

import com.duriancodes.ratingcontrolservice.service.RatingControlService;
import com.tdd.book.swagger.api.RclApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingControlLevelController implements RclApi {
    private static final String ALPHABET_REGEX = "[a-zA-Z]+";
    private static final String NUMBER_REGEX = "[0-9]+";
    private static final String NO_SPECIAL_CHAR_REGEX = "[a-zA-Z0-9]*";

    @Autowired
    private RatingControlService ratingControlService;

    @Override
    public ResponseEntity<Boolean> getControlAccess(String controlLevel, String bookId) {
        if (!isValidControlLevel(controlLevel) || containsSpecialCharacters(bookId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean canReadBook = ratingControlService.canReadBook(controlLevel, bookId);
        return new ResponseEntity<>(canReadBook, HttpStatus.OK);
    }

    public boolean isValidControlLevel(String controlLevel) {
        return controlLevel.matches(ALPHABET_REGEX) || controlLevel.matches(NUMBER_REGEX);
    }

    public boolean containsSpecialCharacters(String bookId) {
        return !bookId.matches(NO_SPECIAL_CHAR_REGEX);
    }
}
