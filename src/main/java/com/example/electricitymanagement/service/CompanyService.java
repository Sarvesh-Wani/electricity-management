package com.example.electricitymanagement.service;

import com.example.electricitymanagement.dto.CreateCompanyRequest;
import com.example.electricitymanagement.entity.master.*;
import com.example.electricitymanagement.enums.CompanyStatus;
import com.example.electricitymanagement.enums.Role;
import com.example.electricitymanagement.repository.AppUserRepository;
import com.example.electricitymanagement.repository.ClientCompanyRepository;
import com.example.electricitymanagement.tenant.TenantProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final ClientCompanyRepository companyRepository;

    private final AppUserRepository userRepository;

    private final TenantProvisioningService tenantProvisioningService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createCompany(CreateCompanyRequest request) {

        if(companyRepository.existsByTenantId(request.getTenantId())) {

            throw new RuntimeException("Tenant already exists");
        }

        ClientCompany company = new ClientCompany();

        company.setCompanyName(request.getCompanyName());

        company.setTenantId(request.getTenantId());

        company.setStatus(CompanyStatus.ACTIVE);

        companyRepository.save(company);

        // Create schema
        tenantProvisioningService.provisionTenant(request.getTenantId());

        // Create first admin user
        AppUser admin = new AppUser();

        admin.setUsername(request.getAdminUsername());

        admin.setEmail(request.getAdminEmail());

        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));

        admin.setRole(Role.CLIENT_OPERATIONS);

        admin.setTenantId(request.getTenantId());

        admin.setActive(true);

        userRepository.save(admin);
    }
}
