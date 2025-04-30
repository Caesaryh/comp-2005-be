package com.caesaryh.comp2005be;
import com.caesaryh.comp2005be.model.Admission;
import com.caesaryh.comp2005be.model.Allocation;
import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import com.caesaryh.comp2005be.service.WebClientAPIServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaternityServiceTest {

    @Mock
    private WebClientAPIServer apiService;

    @InjectMocks
    private MaternityService maternityService;

    static Stream<Arguments> neverDischargedProvider() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Admission(1, now, now.plusDays(1), 101),
                                new Admission(2, now, null, 102)
                        ),
                        List.of(
                                new Patient(101, "Wang", "", "NHS101"),
                                new Patient(102, "Li", "", "NHS102")
                        ),
                        List.of("Li")
                ),
                Arguments.of(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("neverDischargedProvider")
    void testGetNeverDischargedPatients(List<Admission> admissions,
                                        List<Patient> patients,
                                        List<String> expectedSurnames) {
        when(apiService.getAllAdmissions()).thenReturn(admissions);
        when(apiService.getAllPatients()).thenReturn(patients);

        List<Patient> result = maternityService.getNeverDischargedPatients();

        assertEquals(expectedSurnames,
                result.stream().map(Patient::getSurname).collect(Collectors.toList()));
    }

    @Test
    void testReadmissionEdgeCases() {
        LocalDateTime baseTime = LocalDateTime.of(2023, 1, 1, 10, 0);

        List<Admission> admissions = List.of(
                new Admission(1, baseTime, baseTime.plusDays(2), 201),
                new Admission(2, baseTime.plusDays(9).plusHours(23), null, 201),

                new Admission(3, baseTime, baseTime.plusDays(3), 202),
                new Admission(4, baseTime.plusDays(2), null, 202)
        );

        when(apiService.getAllAdmissions()).thenReturn(admissions);
        when(apiService.getAllPatients()).thenReturn(List.of(
                new Patient(201, "Edge", "Case1", "NHS201"),
                new Patient(202, "Invalid", "Case", "NHS202")
        ));

        List<Patient> result = maternityService.getReadmittedWithin7DaysPatients();

        assertEquals(1, result.size());
        assertEquals("Edge", result.get(0).getSurname());
    }

    @Test
    void testBusiestMonthWithTie() {
        List<Admission> admissions = List.of(
                new Admission(1, LocalDateTime.parse("2023-01-05T10:00"), null, 301),
                new Admission(2, LocalDateTime.parse("2023-01-15T14:00"), null, 302),
                new Admission(3, LocalDateTime.parse("2023-02-01T09:00"), null, 303),
                new Admission(4, LocalDateTime.parse("2023-02-10T16:00"), null, 304)
        );

        when(apiService.getAllAdmissions()).thenReturn(admissions);

        String result = maternityService.getBusiestMonth();


        assertTrue(result.equals("2023-01") || result.equals("2023-02"));
    }

    @Test
    void testMultiStaffWithNestedAllocations() {
        List<Allocation> allocations = List.of(
                new Allocation(1, 1001, 901, LocalDateTime.now(), null),
                new Allocation(2, 1001, 902, LocalDateTime.now(), null),
                new Allocation(3, 1002, 903, LocalDateTime.now(), null)
        );

        List<Admission> admissions = List.of(
                new Admission(1001, LocalDateTime.now(), null, 401),
                new Admission(1002, LocalDateTime.now(), null, 402)
        );

        when(apiService.getAllAllocations()).thenReturn(allocations);
        when(apiService.getAllAdmissions()).thenReturn(admissions);
        when(apiService.getAllPatients()).thenReturn(List.of(
                new Patient(401, "Multi", "Staff", "NHS401"),
                new Patient(402, "Single", "Staff", "NHS402")
        ));

        List<Patient> result = maternityService.getPatientsWithMultipleStaff();

        assertEquals(1, result.size());
        assertEquals("Multi", result.get(0).getSurname());
    }

    @Test
    void testEmptyDataScenarios() {

        when(apiService.getAllAdmissions()).thenReturn(Collections.emptyList());
        when(apiService.getAllAllocations()).thenReturn(Collections.emptyList());
        when(apiService.getAllPatients()).thenReturn(Collections.emptyList());

        assertAll(
                () -> assertEquals(0, maternityService.getNeverDischargedPatients().size()),
                () -> assertEquals(0, maternityService.getReadmittedWithin7DaysPatients().size()),
                () -> assertEquals("No data", maternityService.getBusiestMonth()),
                () -> assertEquals(0, maternityService.getPatientsWithMultipleStaff().size())
        );
    }
}