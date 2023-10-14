package com.danbi.recruit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Company {
    @Id @GeneratedValue
    @Column(name = "company_id")
    private Long id;

    @OneToMany(mappedBy = "company")
    private List<Recruit> recruits = new ArrayList<>();

    private String name;

    private String country;

    private String city;
}
