package com.duriancodes.ratingcontrolservice.controller;

import com.duriancodes.ratingcontrolservice.exception.BookNotFoundException;
import com.duriancodes.ratingcontrolservice.service.RatingControlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RatingControlLevelControllerIT {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RatingControlService ratingControlService;

    @Test
    public void shouldReturnTrue_whenBookServiceControlLevelIsEqualTo_CustomerControlLevel_ForRequestBookId() throws Exception {
        given(ratingControlService.canReadBook(anyString(), anyString())).willReturn(true);
        mockMvc.perform(get("/rcl/book/v1/read/eligibility/12/B1234")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void shouldReturnNotFound_whenCustomerControlLevelAndBookIdIsNotProvided() throws Exception {
        mockMvc.perform(get("/rcl/book/v1/read/eligibility/")
                .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequest_whenInvalidCustomerControlLevelAndBookIdIsProvided() throws Exception {
        mockMvc.perform(get("/rcl/book/v1/read/eligibility/CONTROLXXXX*/ID**")
                .accept("application/json"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldInvokeRatingControlService_whenValidCustomerRatingControlLevelAndBookIdIsProvided() throws Exception {
        given(ratingControlService.canReadBook(anyString(), anyString())).willReturn(true);
        mockMvc.perform(get("/rcl/book/v1/read/eligibility/12/B1234")
                .accept("application/json"))
                .andExpect(status().isOk());
        verify(ratingControlService, times(1)).canReadBook(anyString(), anyString());
    }

    @Test
    public void shouldReturnNotFound_whenBookNotFoundExceptionIsThrown() throws Exception {
        given(ratingControlService.canReadBook(anyString(), anyString())).willThrow(new BookNotFoundException("Book not found"));
        mockMvc.perform(get("/rcl/book/v1/read/eligibility/12/B1234")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"404\",\"message\":\"Book not found\"}"));
    }
}
