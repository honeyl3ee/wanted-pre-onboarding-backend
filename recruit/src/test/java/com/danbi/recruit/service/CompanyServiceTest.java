package com.danbi.recruit.service;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class CompanyServiceTest {

    @Autowired
    EntityManager em;
    @Autowired CompanyService companyService;

    @Test
    public void 회사조회() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "서울");
        companyService.join(company);

        //when
        Company findCompany = companyService.findOne(company.getId());


        //then
        assertEquals(findCompany.getId(), company.getId());
        assertEquals(findCompany.getName(), company.getName());
        assertEquals(findCompany.getCountry(), company.getCountry());

    }

    @Test
    public void 중복_회사_예외() throws Exception {
        //given
        Company company1 = createCompany("원티드", "한국", "서울");
        Company company2 = createCompany("원티드", "한국", "서울");

        //when
        companyService.join(company1);

        //then
        assertThrows(IllegalStateException.class, () -> companyService.join(company2), "중복 에러가 발생해야 합니다.");
    }

    @Test
    public void 회사_목록_조회() throws Exception {
        // given
        Company company1 = createCompany("company1", "korea", "seoul");
        Company company2 = createCompany("company2", "korea", "busan");
        companyService.join(company1);
        companyService.join(company2);
        // when
        int size = companyService.findCompanies().size();
        String name = companyService.findOne(company1.getId()).getName();
        String country = companyService.findOne(company1.getId()).getCountry();
        // then
        assertEquals(2, size, "조회된 데이터 개수와 실행시 만들어지는 데이터의 숫자가 같아야합니다.");
        assertEquals("company1", name, "조회된 데이터의 name과 실행시 만들어지는 데이터의 name이 같아야 합니다.");
        assertEquals("korea", country, "조회된 데이터 region와 실행시 만들어지는 데이터 region이 같아야 합니다.");

    }

    public Company createCompany(String name, String country, String city) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setCity(city);
        return company;
    }
}