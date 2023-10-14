package com.danbi.recruit.repository;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecruitRepository {

    private final EntityManager em;

    public void save(Recruit recruit) {
        em.persist(recruit);
    }

    public List<Recruit> findAll() {
        return em.createQuery("select r from Recruit r", Recruit.class).getResultList();
    }

    public Recruit findOne(Long recruitId) {
        return em.find(Recruit.class, recruitId);
    }

    public List<Recruit> findByCompany(Long companyId) {
        Company company = em.find(Company.class, companyId);
        return em.createQuery("select r from Recruit r where r.company = :company", Recruit.class)
                .setParameter("company", company)
                .getResultList();
    }

//    public Optional<List<Recruit>> searchRecruit()

}
