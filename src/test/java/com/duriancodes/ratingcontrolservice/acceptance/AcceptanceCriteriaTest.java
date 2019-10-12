package com.duriancodes.ratingcontrolservice.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "pretty",
        features = "src/test/resources/features/RatingControlLevel.feature",
        glue = "com.duriancodes.ratingcontrolservice.acceptance.glue"
)
public class AcceptanceCriteriaTest {

}
