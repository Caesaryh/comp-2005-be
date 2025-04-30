package com.caesaryh.comp2005be;

import com.caesaryh.comp2005be.model.Admission;
import com.caesaryh.comp2005be.model.Employee;
import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import com.caesaryh.comp2005be.service.WebClientAPIServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebClientAPIServerIntegrationTest {

    @Autowired
    private WebClientAPIServer apiClient;

    @Test
    public void getAllAdmissions_WhenCalled_ShouldReturnValidData() {
        List<Admission> result = apiClient.getAllAdmissions();
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getAdmissionDate().isAfter(LocalDateTime.of(2020,1,1,0,0)));
    }

    @Test
    public void getPatientById_WhenValidId_ShouldReturnCorrectPatient() {
        Patient patient = apiClient.getPatientById(1).block();
        assertEquals("Smith", patient.getSurname());
        assertEquals("NHS1001", patient.getNhsNumber());
    }

    @Test
    public void getAllEmployees_WhenCalled_ShouldContainMedicalRoles() {
        List<Employee> employees = apiClient.getAllEmployees();
        assertTrue(employees.size() > 5);
        assertTrue(employees.stream().anyMatch(e -> e.getSurname().contains("Doctor")));
    }
}
