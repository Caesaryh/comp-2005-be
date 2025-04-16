package com.caesaryh.comp2005be.service;


import com.caesaryh.comp2005be.exception.type.ApiException;
import com.caesaryh.comp2005be.model.Admission;
import com.caesaryh.comp2005be.model.Allocation;
import com.caesaryh.comp2005be.model.Employee;
import com.caesaryh.comp2005be.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebClientAPIServer {

    private final String baseUrl = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    // RestTemplate方式注入
    private final RestTemplate apiRestTemplate;

    // WebClient方式注入
    private final WebClient maternityApiClient;

    public List<Admission> getAllAdmissions() {
        ResponseEntity<List<Admission>> response = apiRestTemplate.exchange(
                baseUrl + "/Admissions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Admission>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ApiException("Failed to get admissions: " + response.getStatusCode());
    }


    public Mono<Admission> getAdmissionById(Integer id) {
        return maternityApiClient
                .get()
                .uri("/Admissions/{id}",id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new ApiException("Admission API error: " + response.statusCode()))
                )
                .bodyToMono(Admission.class);

    }

    public List<Allocation> getAllAllocations() {
        ResponseEntity<List<Allocation>> response = apiRestTemplate.exchange(
                baseUrl + "/Allocations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Allocation>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ApiException("Failed to get patients: " + response.getStatusCode());
    }

    public Mono<Allocation> getAllocationById(Integer id) {
        return maternityApiClient
                .get()
                .uri("/Allocations/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new ApiException("Patient API error: " + response.statusCode()))
                )
                .bodyToMono(Allocation.class);
    }

    public List<Employee> getAllEmployees() {
        ResponseEntity<List<Employee>> response = apiRestTemplate.exchange(
                baseUrl + "/Employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ApiException("Failed to get patients: " + response.getStatusCode());
    }

    public Mono<Employee> getEmployeeById(Integer id) {
        return maternityApiClient
                .get()
                .uri("/Employees/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new ApiException("Patient API error: " + response.statusCode()))
                )
                .bodyToMono(Employee.class);
    }

    public List<Patient> getAllPatients() {
        ResponseEntity<List<Patient>> response = apiRestTemplate.exchange(
                baseUrl + "/Patients",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Patient>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new ApiException("Failed to get patients: " + response.getStatusCode());
    }

    public Mono<Patient> getPatientById(Integer id) {
        return maternityApiClient
                .get()
                .uri("/Patients/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new ApiException("Patient API error: " + response.statusCode()))
                )
                .bodyToMono(Patient.class);
    }
}
