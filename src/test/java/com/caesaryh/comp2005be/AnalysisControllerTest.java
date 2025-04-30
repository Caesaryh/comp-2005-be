package com.caesaryh.comp2005be;

import com.caesaryh.comp2005be.controller.AnalysisController;
import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalysisController.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaternityService maternityService;

    @Test
    void testGetNeverDischarged() throws Exception {
        List<Patient> mockPatients = List.of(new Patient(200, "Tom", "Jerry", "NHS200"));
        when(maternityService.getNeverDischargedPatients()).thenReturn(mockPatients);

        mockMvc.perform(get("/api/f1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nhsNumber").value("NHS200"));
    }

    @Test
    void testGetReadmittedWithin7Days() throws Exception {
        List<Patient> mockPatients = List.of(new Patient(300, "Alice", "Smith", "NHS300"));
        when(maternityService.getReadmittedWithin7DaysPatients()).thenReturn(mockPatients);

        mockMvc.perform(get("/api/f2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].surname").value("Alice"));
    }

    @Test
    void testGetBusiestMonth() throws Exception {
        when(maternityService.getBusiestMonth()).thenReturn("2023-12");

        mockMvc.perform(get("/api/f3"))
                .andExpect(status().isOk())
                .andExpect(content().string("2023-12"));
    }

    @Test
    void testGetMultiStaffPatients() throws Exception {
        List<Patient> mockPatients = List.of(new Patient(400, "Bob", "Wilson", "NHS400"));
        when(maternityService.getPatientsWithMultipleStaff()).thenReturn(mockPatients);

        mockMvc.perform(get("/api/f4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].forename").value("Wilson"));
    }

    @Test
    void testEmptyResponses() throws Exception {
        when(maternityService.getNeverDischargedPatients()).thenReturn(Collections.emptyList());
        when(maternityService.getReadmittedWithin7DaysPatients()).thenReturn(Collections.emptyList());
        when(maternityService.getBusiestMonth()).thenReturn("No data");
        when(maternityService.getPatientsWithMultipleStaff()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/f1")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/f2")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/api/f3")).andExpect(content().string("No data"));
        mockMvc.perform(get("/api/f4")).andExpect(jsonPath("$").isEmpty());
    }
}
