package com.duriancodes.ratingcontrolservice.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BookNotFoundExceptionTest {
    @Rule
    private ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowBookNotFoundExceptionWithMessage_whenBookIsNotFound() {
        expectedException.expect(BookNotFoundException.class);
        expectedException.expectMessage("The book service could not find given book!");
        throw new BookNotFoundException("The book service could not find given book!");
    }
}