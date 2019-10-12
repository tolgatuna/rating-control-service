package com.duriancodes.ratingcontrolservice.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateConfigTest {
    @Autowired
    RestTemplateConfig restTemplateConfig;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void shouldHaveValidBeanCreatedForRestTemplate() {
        assertNotNull("Rest template config is null", restTemplateConfig.restTemplate());
        assertNotNull("Rest template bean is null", restTemplate);
    }
}
