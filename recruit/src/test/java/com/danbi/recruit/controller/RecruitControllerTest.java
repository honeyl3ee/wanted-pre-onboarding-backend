package com.danbi.recruit.controller;

import com.danbi.recruit.DTO.RecruitDTO;
import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.service.CompanyService;
import com.danbi.recruit.service.RecruitService;
import com.danbi.recruit.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RecruitControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EntityManager em;

    @MockBean
    private RecruitService recruitService;

    @MockBean
    private UserService userService;

    @MockBean
    private CompanyService companyService;

    @Before
    public void setup() {
        Company company = createCompany("company1", "korea", "seoul");
        Recruit recruit = Recruit.createRecruit(company, 10000000L,"프론트엔드 개발자 구합니다.", "프론트엔드", "javascript" );
        Users user = createUser("UserA", 1L);

        Company company2 = createCompany("company2", "japan", "tokyo");
        Recruit recruit2 = Recruit.createRecruit(company2, 300L,"백엔드 개발자 구합니다.", "백엔드", "java" );
        Users user2 = createUser("UserB", 2L);

        Recruit recruit3 = Recruit.createRecruit(company, 1000000L,"find frontend developer", "Frontend1","react/typescript" );
        Recruit recruit4 = Recruit.createRecruit(company2, 1000000L,"find frontend developer", "Frontend2","react/typescript1" );
        Recruit recruit5 = Recruit.createRecruit(company, 1000000L,"find frontend developer", "Frontend3","react/typescript2" );
        List<Recruit> searchResults = Arrays.asList(recruit3, recruit4, recruit5);

        List<Recruit> recruits = new ArrayList<>();
        recruits.add(recruit);
        recruits.add(recruit2);

        when(recruitService.findRecruit(1L)).thenReturn(recruit);
        when(recruitService.findRecruit(2L)).thenReturn(recruit2);
        when(companyService.findOne(1L)).thenReturn(company);
        when(companyService.findOne(2L)).thenReturn(company2);
        when(recruitService.findRecruits()).thenReturn(recruits);
        when(userService.findUser(1L)).thenReturn(user);
        when(userService.findUser(2L)).thenReturn(user2);
        when(recruitService.searchRecruit("react")).thenReturn(Optional.of(searchResults));
        when(recruitService.apply(1L, 1L)).thenReturn(1L);

    }

    @Test
    public void 공고_조회() throws Exception {
        mockMvc.perform(get("/api/recruits/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("position").value("프론트엔드"))
                .andExpect(jsonPath("reward").value(10000000L))
                .andExpect(jsonPath("companyName").value("company1"))
                .andReturn();
    }

    @Test
    public void 공고_목록_조회() throws Exception {
        mockMvc.perform(get("/api/recruits").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    public void 공고_조회_상세페이지() throws Exception {
        mockMvc.perform(get("/api/recruits/{id}/detail", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value("프론트엔드 개발자 구합니다."))
                .andExpect(jsonPath("position").value("프론트엔드"))
                .andReturn();
    }

    @Test
    public void 공고_키워드_검색() throws Exception {
        mockMvc.perform(get("/api/recruits/search").param("search", "react").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].position").value("Frontend1"))
                .andExpect(jsonPath("[1].position").value("Frontend2"))
                .andExpect(jsonPath("[2].position").value("Frontend3"));

    }

    @Test
    public void 공고_게시() throws Exception {
        RecruitDTO.CreateRecruitRequest request = new RecruitDTO.CreateRecruitRequest();
        request.setCompanyId(1L);
        request.setPosition("backend");
        request.setReward(1000000L);
        request.setTechStack("java/spring");
        request.setContent("We are looking for a backend developer.");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/recruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void 공고_수정() throws Exception {
        RecruitDTO.UpdateRecruitRequest request = new RecruitDTO.UpdateRecruitRequest("수정된 포지션입니다.", 300L, "수정된 기술입니다.","수정된 컨텐츠 입니다.");

        String requestBody = objectMapper.writeValueAsString(request);
        Company company = new Company();

        Recruit updatedRecruit = Recruit.createRecruit(company, 300L, "수정된 컨텐츠 입니다.", "수정된 포지션입니다.", "수정된 기술입니다.");

        when(recruitService.findRecruit(1L)).thenReturn(updatedRecruit);
        when(recruitService.update(1L, request)).thenReturn(1L);

        mockMvc.perform(put("/api/recruits/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value("수정된 컨텐츠 입니다."))
                .andExpect(jsonPath("position").value("수정된 포지션입니다."))
                .andExpect(jsonPath("techStack").value("수정된 기술입니다."));

    }

    @Test
    public void 공고_지원() throws Exception {
        RecruitDTO.ApplyRequest request = new RecruitDTO.ApplyRequest();
        request.setUserId(1L);
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/recruits/{id}/apply", 1).contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(1L))
                .andExpect(jsonPath("userName").value("UserA"))
                .andExpect(jsonPath("recruitId").value(1L));

    }


    public Company createCompany(String name, String country, String city) {
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setCity(city);

        when(companyService.join(company)).thenReturn(company.getId());
        return company;
    }

    public Users createUser(String name, Long id) {
        Users user = new Users();
        user.setId(id);
        user.setName(name);

        when(userService.join(user)).thenReturn(user.getId());
        return user;
    }


}