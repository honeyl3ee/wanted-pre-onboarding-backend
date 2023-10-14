package com.danbi.recruit.service;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Long join(Company company) {
        validatedDuplicateMember(company);
        companyRepository.save(company);
        return company.getId();
    }

    private void validatedDuplicateMember(Company company) {
        List<Company> findCompanies = companyRepository.findByName(company.getName());
        if (!findCompanies.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회사입니다.");
        }
    }

    public List<Company> findCompanies() {
        return companyRepository.findAll();
    }

    public Company findOne(Long companyId) {
        return companyRepository.findOne(companyId);
    }

}
