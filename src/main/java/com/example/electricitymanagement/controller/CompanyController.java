package com.example.electricitymanagement.controller;

import com.example.electricitymanagement.dto.CreateCompanyRequest;
import com.example.electricitymanagement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/company")
    public ResponseEntity<String> createCompany(@RequestBody CreateCompanyRequest request) {

        companyService.createCompany(request);

        return ResponseEntity.ok("Company created successfully");
    }

    @GetMapping("testing")
    public ResponseEntity<String> testing(){
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }
}
