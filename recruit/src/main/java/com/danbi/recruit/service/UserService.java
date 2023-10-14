package com.danbi.recruit.service;

import com.danbi.recruit.domain.Users;
import com.danbi.recruit.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void join(Users user) {
        userRepository.save(user);
    }
}
