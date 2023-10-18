package com.danbi.recruit.controller;

import com.danbi.recruit.DTO.RecruitDTO;
import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.service.CompanyService;
import com.danbi.recruit.service.RecruitService;
import com.danbi.recruit.service.UserService;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;
    private final CompanyService companyService;
    private final UserService userService;

    @PostMapping("/api/recruits")
    public RecruitDTO.CreateRecruitResponse postRecruit(@RequestBody @Valid RecruitDTO.CreateRecruitRequest request) {

        Company company = companyService.findOne(request.getCompanyId());
        Recruit recruit = Recruit.createRecruit(company, request.getReward(), request.getContent(), request.getPosition(), request.getTechStack());
        Long recruitId = recruitService.post(recruit);

        return new RecruitDTO.CreateRecruitResponse(recruitId);
    }

    @PutMapping("api/recruits/{id}")
    public RecruitDTO.UpdateRecruitResponse updateRecruit(
            @PathVariable("id") Long id,
            @RequestBody @Valid RecruitDTO.UpdateRecruitRequest request) {
        Long recruitId = recruitService.update(id, request);
        return new RecruitDTO.UpdateRecruitResponse(recruitService.findRecruit(recruitId));
    }

    @GetMapping("/api/recruits")
    public List<RecruitDTO.ReadRecruit> findAllRecruits() {
        List<Recruit> findRecruits = recruitService.findRecruits();
        List<RecruitDTO.ReadRecruit> collect = findRecruits.stream()
                .map(m -> new RecruitDTO.ReadRecruit(m.getId() ,m.getCompany().getName(), m.getCompany().getCountry(), m.getCompany().getCity(), m.getPosition(), m.getReward(), m.getTechStack()))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("api/recruits/{id}")
    public RecruitDTO.ReadRecruit findRecruit(@PathVariable("id") Long id) {
        Recruit recruit = recruitService.findRecruit(id);
        return new RecruitDTO.ReadRecruit(recruit);
    }

    @GetMapping("api/recruits/{id}/detail")
    public RecruitDTO.ReadDetailedRecruit findDetailedRecruit(@PathVariable("id") Long id) {
        Recruit recruit = recruitService.findRecruit(id);
        return new RecruitDTO.ReadDetailedRecruit(recruit);
    }

    @GetMapping("api/recruits/search")
    public Optional<List<RecruitDTO.ReadRecruit>> searchRecruit(@RequestParam String search) {
        Optional<List<Recruit>> recruits = recruitService.searchRecruit(search);
        return Optional.of(recruits.orElse(Collections.emptyList())
                .stream()
                .map(m -> new RecruitDTO.ReadRecruit(m.getId(), m.getCompany().getName(), m.getCompany().getCountry(), m.getCompany().getCity(), m.getPosition(), m.getReward(), m.getTechStack()))
                .toList());
    }

    @DeleteMapping("api/recruits/{id}")
    public RecruitDTO.DeleteRecruitResponse deleteRecruit(@PathVariable("id") Long id) {
        Recruit recruit = recruitService.findRecruit(id);
        recruitService.delete(id);
        return new RecruitDTO.DeleteRecruitResponse(id);
    }


    @PatchMapping("api/recruits/{id}/apply")
    public RecruitDTO.ApplyResponse applyRecruit(@PathVariable("id") Long id, @RequestBody @Valid RecruitDTO.ApplyRequest request) {
        Users user = userService.findUser(request.getUserId());

        return new RecruitDTO.ApplyResponse(user, recruitService.apply(id, user.getId()));
    }
}