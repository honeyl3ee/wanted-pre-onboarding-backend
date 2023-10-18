package com.danbi.recruit.service;

import com.danbi.recruit.DTO.RecruitDTO;
import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.repository.CompanyRepository;
import com.danbi.recruit.repository.RecruitRepository;
import com.danbi.recruit.repository.UserRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long post(Recruit recruit) {
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    @Transactional
    public Long update(Long id, RecruitDTO.UpdateRecruitRequest request) {
        Recruit recruit = recruitRepository.findOne(id);
        recruit.setContent(request.getContent());
        recruit.setReward(request.getReward());
        recruit.setPosition(request.getPosition());
        recruit.setTechStack(request.getTechStack());
        return recruit.getId();
    }

    @Transactional
    public void delete(Long recruitId) {
        Recruit recruit = recruitRepository.findOne(recruitId);
        recruitRepository.delete(recruit);
    }

    @Transactional
    public Long apply(Long recruitId, Long userId){
        Recruit recruit = recruitRepository.findOne(recruitId);
        Users user = userRepository.findOne(userId);
        if(user.getRecruit() != null)
            throw new IllegalStateException("이미 지원한 공고가 존재합니다.");
        recruit.setUsers(user);
        return recruitId;
    }


    public Recruit findRecruit(Long recruitId) {
        return recruitRepository.findOne(recruitId);
    }

    public List<Recruit> findRecruits() {
        return recruitRepository.findAll();
    }

    public Optional<List<Recruit>> searchRecruit(String keyword) {
        return recruitRepository.searchRecruit(keyword);
    }

}
