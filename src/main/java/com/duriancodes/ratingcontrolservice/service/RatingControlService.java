package com.duriancodes.ratingcontrolservice.service;

public interface RatingControlService {
    boolean canReadBook(final String customerRatingControlLevel, final String bookId);
}
