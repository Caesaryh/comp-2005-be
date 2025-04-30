package com.caesaryh.comp2005be;


import com.caesaryh.comp2005be.controller.AnalysisController;
import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalysisController.class)
public class AnalysisControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaternityService maternityService;

    @Test
    public void f1Endpoint_WhenCalled_ShouldReturn200() throws Exception {
        when(maternityService.getNeverDischargedPatients()).thenReturn(List.of(
            new Patient(5, "Doe", "John", "NHS1005")
        ));

        mockMvc.perform(get("/api/f1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nhsNumber").value("NHS1005"));
    }

    @Test
    public void f3Endpoint_WhenNoData_ShouldReturnDefault() throws Exception {
        when(maternityService.getBusiestMonth()).thenReturn("No data");
        
        mockMvc.perform(get("/api/f3"))
                .andExpect(status().isOk())
                .andExpect(content().string("No data"));
    }

    @Test
    public void f4Endpoint_WhenMultiStaff_ShouldReturnFiltered() throws Exception {
        when(maternityService.getPatientsWithMultipleStaff()).thenReturn(List.of(
            new Patient(3, "Smith", "Emma", "NHS1003")
        ));

        mockMvc.perform(get("/api/f4"))
                .andExpect(jsonPath("$[0].surname").value("Smith"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
