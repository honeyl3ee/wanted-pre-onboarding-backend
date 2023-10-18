package com.danbi.recruit.service;

import com.danbi.recruit.DTO.RecruitDTO;
import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.repository.RecruitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Recruit recruit = Recruit.createRecruit(company, 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Long recruitId = recruitService.post(recruit);

        //when
        Recruit findRecruit = recruitRepository.findOne(recruitId);

        //then
        assertEquals(recruitId, findRecruit.getId());
        assertEquals(company.getId(), findRecruit.getCompany().getId());
        assertEquals("백엔드", findRecruit.getPosition());
    }

    @Test
    public void 채용공고삭제() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Recruit recruit = Recruit.createRecruit(company, 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Long recruitId = recruitService.post(recruit);

        //when
        recruitService.delete(recruitId);

        //then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> recruitService.findRecruit(recruitId), "존재하지 않는 채용공고 조회 시 에러가 발생해야 한다.");
    }

    @Test
    public void 채용공고지원_삭제() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Recruit recruit = Recruit.createRecruit(company, 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Long recruitId = recruitService.post(recruit);
        Users user = createUser("kim");
        Recruit findRecruit = recruitService.findRecruit(recruitId);

        //when
        recruitService.apply(recruitId, user.getId());
        recruitService.delete(recruitId);

        //then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> recruitService.findRecruit(recruitId), "존재하지 않는 채용공고 조회 시 에러가 발생해야 한다.");
        assertEquals(user.getRecruit(), findRecruit);
    }

    @Test
    public void 채용공고지원() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Recruit recruit = Recruit.createRecruit(company, 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Long recruitId = recruitService.post(recruit);
        Users user = createUser("kim");
        Recruit findRecruit = recruitService.findRecruit(recruitId);

        //when
        recruitService.apply(findRecruit.getId(), user.getId());

        //then
        assertTrue(findRecruit.getUsers().contains(user));
        assertEquals(1, findRecruit.getUsers().size());
        assertThrows(IllegalStateException.class, () -> recruitService.apply(findRecruit.getId(), user.getId()), "중복 지원 시 에러가 발생해야 한다.");
    }

    @Test
    public void 채용공고_수정() throws Exception {
        //given
        Company company = createCompany("원티드", "한국", "부산");
        Recruit recruit = Recruit.createRecruit(company, 1000000L, "백엔드 개발자를 모집합니다.", "백엔드", "java");
        Long recruitId = recruitService.post(recruit);

        //when
        RecruitDTO.UpdateRecruitRequest request = new RecruitDTO.UpdateRecruitRequest("프론트", 1000000L, "javascript", "프론트엔드 개발자를 모집합니다.");
        recruitService.update(recruitId, request);

        Recruit findRecruit = recruitRepository.findOne(recruitId);

        //then
        assertEquals(findRecruit.getId(), recruitId);
        assertEquals("프론트", findRecruit.getPosition());
        assertEquals(recruit.getCompany().getId(), findRecruit.getCompany().getId());
    }

    @Test
    public void 채용공고_조회() throws Exception {

        //when
        Recruit recruit = recruitService.findRecruit(1L);
        Recruit findRecruit = recruitRepository.findOne(1L);

        //then
        assertEquals(recruit, findRecruit);
    }

    @Test
    public void 채용공고_목록_조회() throws Exception {

        //when
        List<Recruit> recruits = recruitService.findRecruits();

        //then
        assertEquals(4, recruits.size());
    }

    @Test
    public void 채용공고_키워드_검색() throws Exception {
        //when
        Optional<List<Recruit>> recruits = recruitService.searchRecruit("flutter");

        //given
        assertTrue(recruits.isPresent());
        assertEquals(1, recruits.get().size());
    }


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