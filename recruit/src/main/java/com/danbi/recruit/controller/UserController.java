package com.danbi.recruit.controller;

import com.danbi.recruit.domain.Users;
import com.danbi.recruit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public Result findAllUsers() {
        List<Users> findUsers = userService.findAllUsers();

        List<UserDto> collect = findUsers.stream()
                .map(m -> new UserDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class UserDto {
        private String name;
    }
}
