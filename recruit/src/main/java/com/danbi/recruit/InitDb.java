package com.danbi.recruit;

import com.danbi.recruit.domain.Company;
import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit() {
            Users user1 = new Users();
            user1.setName("userA");
            em.persist(user1);

            Users user2 = new Users();
            user2.setName("userB");
            em.persist(user2);

            Company company = new Company();
            company.setName("company1");
            company.setCountry("korea");
            company.setCity("seoul");
            em.persist(company);

            Company company2 = new Company();
            company2.setName("company2");
            company2.setCountry("korea");
            company2.setCity("busan");
            em.persist(company2);

            Recruit recruit1 = Recruit.createRecruit(company2, 1000000L, "백엔드 개발자 구합니다.", "백엔드", "java");
            em.persist(recruit1);

            Recruit recruit2 = Recruit.createRecruit(company, 1000000L, "백엔드 개발자 구합니다.", "백엔드", "java");
            em.persist(recruit2);

            Recruit recruit3 = Recruit.createRecruit(company2, 1000000L, "풀스택 구함", "백엔드", "flutter");
            em.persist(recruit3);

            Recruit recruit4 = Recruit.createRecruit(company, 1000000L, "ios 개발자 구합니다.", "백엔드", "node.js");
            em.persist(recruit4);
        }
    }
}
