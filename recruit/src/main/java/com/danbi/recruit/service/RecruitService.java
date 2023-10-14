package com.danbi.recruit.service;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.RecruitStatus;
import com.danbi.recruit.domain.Users;
import com.danbi.recruit.repository.CompanyRepository;
import com.danbi.recruit.repository.RecruitRepository;
import com.danbi.recruit.repository.UserRepository;
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
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long post(Long companyId, Long reward, String content, String position, String techStack) {
        Company company = companyRepository.findOne(companyId);
        Recruit recruit = Recruit.createRecruit(company, reward, content, position, techStack);
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    @Transactional
    public void delete(Long recruitId) {
        Recruit recruit = recruitRepository.findOne(recruitId);
        recruit.delete();
    }

    @Transactional
    public void apply(Long recruitId, Long userId) throws Exception {
        Recruit recruit = recruitRepository.findOne(recruitId);
        Users user = userRepository.findOne(userId);
        if(user.getRecruit() != null)
            throw new IllegalStateException("이미 지원한 공고가 존재합니다.");
        recruit.setUsers(user);
    }


    public Recruit findRecruit(Long recruitId) {
        return recruitRepository.findOne(recruitId);
    }

    public List<Recruit> findRecruits() {
        return recruitRepository.findAll();
    }

    //상세페이지 반환

    //검색
//    public Optional<List<Recruit>> searchRecruit() {
//        return em.createQuery("select o from Order o join o.member m" + " where o.status = :status" + " and m.name like :name", Orders.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();
//    }


}
