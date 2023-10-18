package com.danbi.recruit.DTO;

import com.danbi.recruit.domain.Recruit;
import com.danbi.recruit.domain.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitDTO {
    @Data
    public static class CreateRecruitRequest {
        @NotNull
        private Long companyId;

        @NotEmpty
        private String position;

        @NotNull
        private Long reward;

        @NotEmpty
        private String techStack;

        @NotEmpty
        private String content;
    }

    @Data
    public static class CreateRecruitResponse {
        private Long id;

        public CreateRecruitResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRecruitRequest {
        private String position;

        private Long reward;

        private String techStack;

        private String content;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRecruitResponse {
        private Long recruitId;

        private String position;

        private Long reward;

        private String techStack;

        private String content;

        public UpdateRecruitResponse(Recruit recruit) {
            this.recruitId = recruit.getId();
            this.position = recruit.getPosition();
            this.reward = recruit.getReward();
            this.techStack = recruit.getTechStack();
            this.content = recruit.getContent();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReadRecruit {
        private Long recruitId;

        private String companyName;

        private String country;

        private String city;

        private String position;

        private Long reward;

        private String techStack;

        public ReadRecruit(Recruit recruit) {
            this.recruitId = recruit.getId();
            this.companyName = recruit.getCompany().getName();
            this.country = recruit.getCompany().getCountry();
            this.city = recruit.getCompany().getCity();
            this.position = recruit.getPosition();
            this.reward = recruit.getReward();
            this.techStack = recruit.getTechStack();
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReadDetailedRecruit {
        private Long recruitId;

        private String companyName;

        private String country;

        private String city;

        private String position;

        private Long reward;

        private String techStack;

        private String content;

        private List<Long> otherRecruitList;

        public ReadDetailedRecruit(Recruit recruit) {
            this.recruitId = recruit.getId();
            this.companyName = recruit.getCompany().getName();
            this.country = recruit.getCompany().getCountry();
            this.city = recruit.getCompany().getCity();
            this.position = recruit.getPosition();
            this.reward = recruit.getReward();
            this.techStack = recruit.getTechStack();
            this.content = recruit.getContent();
            this.otherRecruitList = recruit.getCompany().getRecruits()
                    .stream().map(Recruit::getId).collect(Collectors.toList());
        }
    }

    @Data
    public static class ApplyRequest {
        @NotNull
        private Long userId;
    }

    @Data
    public static class ApplyResponse {
        private Long userId;
        private String userName;
        private Long recruitId;

        public ApplyResponse(Users user, Long recruitId) {
            this.userId = user.getId();
            this.userName = user.getName();
            this.recruitId = recruitId;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteRecruitResponse {
        private Long deletedId;
    }

}
