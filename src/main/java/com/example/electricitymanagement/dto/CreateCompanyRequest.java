package com.example.electricitymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCompanyRequest {

    private String companyName;

    private String tenantId;

    private String adminUsername;

    private String adminEmail;

    private String adminPassword;
}