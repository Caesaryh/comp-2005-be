package com.caesaryh.comp2005be.service;

import com.caesaryh.comp2005be.model.Admission;
import com.caesaryh.comp2005be.model.Allocation;
import com.caesaryh.comp2005be.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaternityService {

    private final WebClientAPIServer apiService;

    public List<Patient> getNeverDischargedPatients() {
        List<Admission> allAdmissions = apiService.getAllAdmissions(); // 假设同步调用
        List<Patient> allPatients = apiService.getAllPatients();

        Set<Integer> dischargedPatientIds = allAdmissions.stream()
                .filter(admission -> admission.getDischargeDate() != null)
                .map(Admission::getPatientID)
                .collect(Collectors.toSet());

        return allPatients.stream()
                .filter(patient -> !dischargedPatientIds.contains(patient.getId()))
                .collect(Collectors.toList());
    }

    public List<Patient> getReadmittedWithin7DaysPatients() {
        List<Admission> admissions = apiService.getAllAdmissions();

        Map<Integer, List<Admission>> patientAdmissions = admissions.stream()
                .collect(Collectors.groupingBy(Admission::getPatientID));

        Set<Integer> resultPatientIds = new HashSet<>();

        patientAdmissions.forEach((patientId, admissionsList) -> {

            admissionsList.sort(Comparator.comparing(Admission::getAdmissionDate));

            for (int i = 1; i < admissionsList.size(); i++) {
                Admission previous = admissionsList.get(i - 1);
                Admission current = admissionsList.get(i);

                if (previous.getDischargeDate() != null) {
                    long daysBetween = ChronoUnit.DAYS.between(
                            previous.getDischargeDate(),
                            current.getAdmissionDate()
                    );
                    if (daysBetween <= 7 && daysBetween >= 0) {
                        resultPatientIds.add(patientId);
                        break;
                    }
                }
            }
        });

        return apiService.getAllPatients().stream()
                .filter(patient -> resultPatientIds.contains(patient.getId()))
                .collect(Collectors.toList());
    }

    public String getBusiestMonth() {
        List<Admission> admissions = apiService.getAllAdmissions();

        Map<YearMonth, Long> monthlyCount = admissions.stream()
                .collect(Collectors.groupingBy(admission ->
                                YearMonth.from(admission.getAdmissionDate()),
                        Collectors.counting()));

        return monthlyCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().toString())
                .orElse("No data");
    }

    public List<Patient> getPatientsWithMultipleStaff() {
        List<Allocation> allocations = apiService.getAllAllocations();
        List<Admission> admissions = apiService.getAllAdmissions();

        Map<Integer, Integer> admissionPatientMap = admissions.stream()
                .collect(Collectors.toMap(Admission::getId, Admission::getPatientID));

        Map<Integer, Set<Integer>> admissionStaffCount = allocations.stream()
                .collect(Collectors.groupingBy(Allocation::getAdmissionID,
                        Collectors.mapping(Allocation::getEmployeeID, Collectors.toSet())));

        Set<Integer> targetPatientIds = admissionStaffCount.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> admissionPatientMap.get(entry.getKey()))
                .collect(Collectors.toSet());

        return apiService.getAllPatients().stream()
                .filter(patient -> targetPatientIds.contains(patient.getId()))
                .collect(Collectors.toList());
    }
}
