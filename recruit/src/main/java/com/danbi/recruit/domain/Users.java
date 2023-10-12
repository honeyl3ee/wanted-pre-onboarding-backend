package com.danbi.recruit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

}
