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
    @Autowired CompanyRepository companyRepository;
    @Autowired CompanyService companyService;

    @Test
    public void 회사조회() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "서울");
        companyRepository.save(company);

        //when
        Company findCompany = companyRepository.findOne(company.getId());


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
        companyService.save(company1);

        //then
        assertThrows(IllegalStateException.class, () -> companyService.save(company2), "중복 에러가 발생해야 합니다.");
    }

    @Test
    public void 회사_목록_조회() throws Exception {
        //given

        //when

        //then
    }

    public Company createCompany(String name, String country, String city) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setCity(city);
//        em.persist(company);
        return company;
    }
}