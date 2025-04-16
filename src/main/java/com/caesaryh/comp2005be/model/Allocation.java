package com.caesaryh.comp2005be.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Allocation {
    private Integer id;

    @JsonProperty("admissionID")
    private Integer admissionID;

    @JsonProperty("employeeID")
    private Integer employeeID;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;
}