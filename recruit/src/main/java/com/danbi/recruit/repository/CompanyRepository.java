package com.danbi.recruit.repository;

import com.danbi.recruit.domain.Company;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {

    private final EntityManager em;

    public void save(Company company) {
        em.persist(company);
    }

    public Company findOne(Long companyId) {
        return em.find(Company.class, companyId);
    }

    public List<Company> findAll() {
        return em.createQuery("select c from Company c", Company.class).getResultList();
    }

    public List<Company> findByName(String name) {
        return em.createQuery("select c from Company c where c.name = :name", Company.class)
                .setParameter("name", name)
                .getResultList();
    }
}
