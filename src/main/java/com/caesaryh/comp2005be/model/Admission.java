package com.caesaryh.comp2005be.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Admission {
    private Integer id;

    @JsonProperty("admissionDate")
    private LocalDateTime admissionDate;

    @JsonProperty("dischargeDate")
    private LocalDateTime dischargeDate;

    @JsonProperty("patientID")
    private Integer patientID;
}