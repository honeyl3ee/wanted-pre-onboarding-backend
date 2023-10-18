package com.danbi.recruit.repository;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
        Recruit findRecruit = em.find(Recruit.class, recruitId);
        if (findRecruit == null)
            throw new InvalidDataAccessApiUsageException("존재하지 않는 공고 id 입니다.");
        return findRecruit;
    }

    public List<Recruit> findByCompany(Long companyId) {
        Company company = em.find(Company.class, companyId);
        return em.createQuery("select r from Recruit r where r.company = :company", Recruit.class)
                .setParameter("company", company)
                .getResultList();
    }

    public void delete(Recruit recruit) {
        em.remove(recruit);
    }

    public Optional<List<Recruit>> searchRecruit(String keyword) {
        String jpqlQuery = "select r from Recruit r " +
                "where r.position like :keyword " +
                "or r.techStack like :keyword " +
                "or r.company.name like :keyword " +
                "or r.company.country like :keyword " +
                "or r.company.city like :keyword";

        TypedQuery<Recruit> query = em.createQuery(jpqlQuery, Recruit.class);
        query.setParameter("keyword", "%" + keyword + "%");

        List<Recruit> results = query.getResultList();

        return Optional.ofNullable(results);
    }

    //public Optional<List<Recruit>> searchRecruit() {
//        return em.createQuery("select o from Order o join o.member m" + " where o.status = :status" +
//        " and m.name like :name", Orders.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();
//    }

}
