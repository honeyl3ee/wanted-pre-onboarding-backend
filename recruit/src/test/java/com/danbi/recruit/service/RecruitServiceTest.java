package com.danbi.recruit.service;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.RecruitStatus;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.repository.RecruitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RecruitServiceTest {

    @Autowired RecruitService recruitService;
    @Autowired CompanyService companyService;
    @Autowired
    RecruitRepository recruitRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 채용공고등록() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Long recruitId = recruitService.post(company.getId(), 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");

        //when
        Recruit findRecruit = recruitRepository.findOne(recruitId);

        //then
        assertEquals(recruitId, findRecruit.getId());
        assertEquals(company.getId(), findRecruit.getCompany().getId());
        assertEquals("백엔드", findRecruit.getPosition());
        assertEquals(RecruitStatus.VALID, findRecruit.getStatus());
    }

    @Test
    public void 채용공고삭제() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Long recruitId = recruitService.post(company.getId(), 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");

        //when
        recruitService.delete(recruitId);
        Recruit findRecruit = recruitService.findRecruit(recruitId);

        //then
        assertEquals(RecruitStatus.INVALID, findRecruit.getStatus());
    }

    @Test
    public void 채용공고지원() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Long recruitId = recruitService.post(company.getId(), 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Users user = createUser("kim");
        Recruit findRecruit = recruitService.findRecruit(recruitId);

        //when
        recruitService.apply(findRecruit.getId(), user.getId());

        //then
        assertTrue(findRecruit.getUsers().contains(user));
        assertEquals(1, findRecruit.getUsers().size());
    }

//    @Test
//    public void 공고조회() throws Exception {
//        //given
//
//        //when
//
//        //then
//    }

    public Company createCompany(String name, String country, String city) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setCity(city);
        companyService.join(company);
        return company;
    }

    public Users createUser(String name) {
        Users user = new Users();
        user.setName(name);
        em.persist(user);
        return user;
    }
}