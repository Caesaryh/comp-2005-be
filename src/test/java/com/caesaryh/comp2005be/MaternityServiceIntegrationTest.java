package com.caesaryh.comp2005be;

import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MaternityServiceIntegrationTest {

    @Autowired
    private MaternityService maternityService;

    @Test
    public void getNeverDischargedPatients_WhenMixedData_ShouldFilterCorrectly() {
        List<Patient> result = maternityService.getNeverDischargedPatients();
        assertNotNull(result);
        assertTrue(result.stream().noneMatch(p -> p.getId() == 1));
        assertTrue(result.stream().anyMatch(p -> p.getId() == 5));
    }

    @Test
    public void getReadmittedWithin7Days_WhenValidSequence_ShouldDetect() {
        List<Patient> result = maternityService.getReadmittedWithin7DaysPatients();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getNhsNumber().equals("NHS1002")));
    }

    @Test
    public void getBusiestMonth_WhenMultipleAdmissions_ShouldReturnMax() {
        String result = maternityService.getBusiestMonth();
        assertEquals("2023-12", result);
    }

    @Test
    public void getPatientsWithMultipleStaff_WhenComplexAllocations_ShouldIdentify() {
        List<Patient> result = maternityService.getPatientsWithMultipleStaff();
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getForename().contains("Sarah")));
    }
}
