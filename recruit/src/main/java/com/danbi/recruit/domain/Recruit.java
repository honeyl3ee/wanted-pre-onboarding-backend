package com.danbi.recruit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id @GeneratedValue
    @Column(name = "recruit_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();

    private String position;

    private Long reward;

    private String techStack;

    private  String content;

    //연관관계 메서드//
    public void setCompany(Company company) {
        this.company = company;
        company.getRecruits().add(this);
    }

    public void setUsers(Users users) {
        this.users.add(users);
        users.setRecruit(this);
    }

    //생성자 메서드//
    public static Recruit createRecruit(Company company, Long reward, String content, String position, String techStack) {
        Recruit recruit = new Recruit();
        recruit.setCompany(company);
        recruit.setReward(reward);
        recruit.setContent(content);
        recruit.setPosition(position);
        recruit.setTechStack(techStack);
        return recruit;
    }
}
