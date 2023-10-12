package com.danbi.recruit.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter

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
