package com.example.electricitymanagement.repository;

import com.example.electricitymanagement.entity.master.ClientCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {

    Optional<ClientCompany> findByTenantId(String tenantId);

    boolean existsByTenantId(String tenantId);
}

