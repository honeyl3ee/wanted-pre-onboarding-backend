package com.danbi.recruit.repository;

import com.danbi.recruit.domain.Users;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(Users user) {
        em.persist(user);
    }

    public Users findOne(Long userId) {
        Users findUser = em.find(Users.class, userId);
        if (findUser == null)
            throw new InvalidDataAccessApiUsageException("존재하지 않는 회사 id 입니다.");
        return findUser;
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class).getResultList();
    }
}
