package com.caesaryh.comp2005be;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void invalidEndpoint_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

}
