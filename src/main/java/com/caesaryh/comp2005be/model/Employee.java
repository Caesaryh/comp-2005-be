package com.caesaryh.comp2005be.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    private Integer id;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("forename")
    private String forename;
}