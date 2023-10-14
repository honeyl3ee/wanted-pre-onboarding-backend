package com.danbi.recruit.controller;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.repository.CompanyRepository;
import com.danbi.recruit.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/api/companies")
    public CreateCompanyResponse saveCompany(@RequestBody @Valid CreateCompanyRequest request) {
        Company company = new Company();
        company.setName(request.name);
        company.setCountry(request.country);
        company.setCity(request.city);

        Long companyId = companyService.join(company);

        return new CreateCompanyResponse(companyId);
    }

    @Data
    static class CreateCompanyRequest {
        @NotEmpty
        private String name;

        @NotEmpty
        private String country;

        @NotEmpty
        private String city;
    }

    @Data
    static class CreateCompanyResponse {
        private Long id;

        public CreateCompanyResponse(Long id) {
            this.id = id;
        }
    }
}
