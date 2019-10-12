package com.duriancodes.ratingcontrolservice.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TechnicalFailureExceptionTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowTechnicalFailureException_whenTechnicalFailureExceptionIsThrown() throws Exception {
        expectedException.expect(TechnicalFailureException.class);
        expectedException.expectMessage("System Error");
        throw new TechnicalFailureException("System Error");
    }
}