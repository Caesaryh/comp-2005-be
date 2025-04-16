package com.caesaryh.comp2005be.controller;

import com.caesaryh.comp2005be.model.Patient;
import com.caesaryh.comp2005be.service.MaternityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final MaternityService maternityService;

    @Autowired
    public AnalysisController(MaternityService maternityService) {
        this.maternityService = maternityService;
    }

    @GetMapping("/f1")
    public ResponseEntity<List<Patient>> getNeverDischarged() {
        return ResponseEntity.ok(maternityService.getNeverDischargedPatients());
    }

    @GetMapping("/f2")
    public ResponseEntity<List<Patient>> getReadmittedWithin7Days() {
        return ResponseEntity.ok(maternityService.getReadmittedWithin7DaysPatients());
    }

    @GetMapping("/f3")
    public ResponseEntity<String> getBusiestMonth() {
        return ResponseEntity.ok(maternityService.getBusiestMonth());
    }

    @GetMapping("/f4")
    public ResponseEntity<List<Patient>> getMultiStaffPatients() {
        return ResponseEntity.ok(maternityService.getPatientsWithMultipleStaff());
    }
}
